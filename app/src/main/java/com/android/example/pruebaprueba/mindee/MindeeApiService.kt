package com.android.example.pruebaprueba.mindeeocr

import android.util.Log
import com.android.example.pruebaprueba.models.Product
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import android.os.Handler
import android.os.Looper
import com.android.example.pruebaprueba.models.mindee.InferenceResponse
import com.android.example.pruebaprueba.models.mindee.MindeeJobResponse
import com.squareup.moshi.Moshi


class MindeeApiService2(private val apiKey: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    // Paso 1: Enviar archivo a la cola
    fun enqueueReceipt(
        imageFile: File,
        modelId: String,
        onSuccess: (String, List<Product>, String, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val mediaType = "image/jpeg".toMediaTypeOrNull()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("model_id", modelId) // 👈 ahora obligatorio
            .addFormDataPart("file", imageFile.name, imageFile.asRequestBody(mediaType))
            .addFormDataPart("raw_text", "true")
            .addFormDataPart("confidence", "true")
            .build()

        val request = Request.Builder()
            .url("https://api-v2.mindee.net/v2/inferences/enqueue")
            .addHeader("Authorization", "Token $apiKey")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error desconocido en enqueue")
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                if (!response.isSuccessful || res == null) {
                    onError("Error enqueue: ${response.code}\n$res")
                    return
                }

                try {
                    val json = JSONObject(res)
                    val job = json.getJSONObject("job")
                    val jobId = job.getString("id")
                    val pollingUrl = job.getString("polling_url")
                    val resultUrl = job.getString("result_url")

                    // Paso 2: hacer polling hasta que termine
                    pollJob(jobId, resultUrl, onSuccess, onError)

                } catch (e: Exception) {
                    onError("Error procesando enqueue: ${e.message}")
                }
            }
        })
    }

    // Paso 2: Polling al estado del job
    private fun pollJob(
        jobId: String,
        resultUrl: String,
        onSuccess: (String, List<Product>, String, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = Request.Builder()
            .url("https://api-v2.mindee.net/v2/jobs/$jobId")
            .addHeader("Authorization", "Token $apiKey")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Error polling: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                if (!response.isSuccessful || res == null) {
                    onError("Error polling: ${response.code}\n$res")
                    return
                }

                try {
                    val json = JSONObject(res)
                    val job = json.getJSONObject("job")
                    val status = job.getString("status")

                    when (status) {
                        "Processing" -> {
                            // esperar un poco y volver a preguntar
                            Handler(Looper.getMainLooper()).postDelayed({
                                pollJob(jobId, resultUrl, onSuccess, onError)
                            }, 2000) // 2 segundos
                        }
                        "Failed" -> {
                            onError("El procesamiento falló: ${job.optJSONObject("error")}")
                        }
                        "Processed" -> {
                            // Paso 3: obtener resultado final
                            getResult(resultUrl, onSuccess, onError)
                        }
                    }

                } catch (e: Exception) {
                    onError("Error procesando polling: ${e.message}")
                }
            }
        })
    }

    // Paso 3: Obtener resultado de la inferencia
    private fun getResult(
        resultUrl: String,
        onSuccess: (String, List<Product>, String, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = Request.Builder()
            .url(resultUrl)
            .addHeader("Authorization", "Token $apiKey")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Error en getResult: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                if (!response.isSuccessful || res == null) {
                    onError("Error en getResult: ${response.code}\n$res")
                    return
                }

                try {
                    val json = JSONObject(res)
                    val inference = json.getJSONObject("inference")
                    val result = inference.getJSONObject("result")
                    val fields = result.getJSONObject("fields")

                    val supplier = fields.optJSONObject("supplier_name")?.optString("value") ?: "Desconocido"
                    val date = fields.optJSONObject("invoice_date")?.optString("value") ?: ""
                    val total = fields.optJSONObject("total_amount")?.optDouble("value") ?: 0.0

                    val products = mutableListOf<Product>()
                    val lineItems = fields.optJSONArray("line_items")
                    if (lineItems != null) {
                        for (i in 0 until lineItems.length()) {
                            val item = lineItems.getJSONObject(i)
                            val description = item.optJSONObject("description")?.optString("value") ?: "Item"
                            val quantity = item.optJSONObject("quantity")?.optDouble("value")?.toInt() ?: 1
                            val unitPrice = item.optJSONObject("unit_price")?.optDouble("value") ?: 0.0
                            val totalAmount = item.optJSONObject("total_amount")?.optDouble("value") ?: 0.0

                            products.add(Product(quantity, description, unitPrice, totalAmount))
                        }
                    }

                    onSuccess(supplier, products, date, total)

                } catch (e: Exception) {
                    onError("Error procesando resultado: ${e.message}")
                }
            }
        })
    }
}

class MindeeService(private val apiKey: String) {

    private val client = OkHttpClient()
    private val baseUrl = "https://api-v2.mindee.net/v2/inferences/enqueue"

    private val moshi = Moshi.Builder().build()
    private val adapter = moshi.adapter(MindeeJobResponse::class.java)

    //POST
    fun sendRequest(
        imageFile: File,
        modelId: String,
        callback: (MindeeJobResponse?, Exception?) -> Unit
    ) {
        // Construir multipart con file + model_id
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                imageFile.name,
                imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .addFormDataPart("model_id", modelId)
            .build()

        val request = Request.Builder()
            .url(baseUrl)
            .header("Authorization", "$apiKey")
            .post(requestBody)
            .build()

        // Ejecutar en background
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback(null, IOException("Error HTTP: ${it.code}"))
                    } else {
                        val body = it.body?.string()
                        if (body != null) {
                            try {
                                val parsed = adapter.fromJson(body)
                                callback(parsed, null)
                            } catch (ex: Exception) {
                                callback(null, ex)
                            }
                        } else {
                            callback(null, IOException("Respuesta vacía"))
                        }
                    }
                }
            }
        })
    }


    //GET
    private val adapterInference = moshi.adapter(InferenceResponse::class.java)


    fun JobStatusResponse(
        pollingUrl:String,
        callback: (InferenceResponse?, Exception?) -> Unit
    ) {

        val request = Request.Builder()
            .url(pollingUrl)
            .header("Authorization", "$apiKey")
            .get()
            .build()

        // Ejecutar en background
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR CONSULTA", "Error de red: ${e.message}")
                Log.e("ERROR CONSULTA", "Causa: ${e.cause}")
                e.printStackTrace()
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e("ERROR CONSULTA", "HTTP no exitoso: ${it.code}")
                        Log.e("ERROR CONSULTA", "Respuesta: ${it.message}")
                        Log.e("ERROR CONSULTA", "Cuerpo: ${it.body?.string()}")
                        callback(null, IOException("Error HTTP: ${it.code}"))
                    } else {
                        val body = it.body?.string()
                        if (body != null) {

                            try {
                                val parsed = adapterInference.fromJson(body)
                                callback(parsed, null)
                            } catch (ex: Exception) {
                                Log.e("ERROR CONSULTA", "Error al parsear JSON: ${ex.localizedMessage}")
                                Log.e("ERROR CONSULTA", "Tipo de error: ${ex::class.java.name}")
                                ex.printStackTrace()
                                callback(null, ex)
                            }
                        } else {
                            Log.e("ERROR CONSULTA", "El cuerpo de la respuesta está vacío")
                            callback(null, IOException("Respuesta vacía"))
                        }
                    }
                }
            }

        })
    }

}
/*
class MindeeApiService(private val apiKey: String) {
    //private val client = OkHttpClient()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()


    fun sendReceipt(
        imageFile: File,
        onSuccess: (String, List<Product>, String, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val mediaType = "image/jpeg".toMediaTypeOrNull()
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("document", imageFile.name, imageFile.asRequestBody(mediaType))
            .build()

        val request = Request.Builder()
           // .url("https://api.mindee.net/products/expense_receipts/v2/predict")
            .url("https://api.mindee.net/v1/products/mindee/expense_receipts/v2/predict")
            .addHeader("Authorization", "Token $apiKey")
            .addHeader("Content-Type", "multipart/form-data")
            .post(body)
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error desconocido")
                Log.d("MindeeAPI", "Code: ${e.cause}, Message: ${e.message}")
                Log.d("MindeeAPI", "Body: ${e.stackTrace}")
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                Log.d("MindeeAPI", "Code: ${response.code}, Message: ${response.message}")
                Log.d("MindeeAPI", "Body: $res")
                if (!response.isSuccessful || res == null) {
                    onError("Error: ${response.code}\n$res")
                    return
                }

                try {
                    val json = JSONObject(res)
                    val prediction = json.getJSONObject("document")
                        .getJSONObject("inference")
                        .getJSONObject("prediction")

                    val supplier = prediction.optJSONObject("supplier_name")?.optString("value") ?: "Desconocido"
                    val date = prediction.optJSONObject("date")?.optString("value") ?: ""
                    val total = prediction.optJSONObject("total_amount")?.optDouble("value") ?: 0.0
                    //val facturaId = factura?.let { db.facturaDao().insertarFactura(it).toInt() }
                    val lineItems = prediction.optJSONArray("line_items")
                    val products = mutableListOf<Product>()

                    if (lineItems != null) {
                        for (i in 0 until lineItems.length()) {
                            val item = lineItems.getJSONObject(i)

                            val description = try {
                                item.getString("description")
                            } catch (e: Exception) {
                                try {
                                    item.getJSONObject("description")?.optString("value") ?: "Item"
                                } catch (e2: Exception) {
                                    "Item"
                                }
                            }

                            val quantity = try {
                                item.getDouble("quantity").toInt()
                            } catch (e: Exception) {
                                1
                            }

                            val unitPrice = try {
                                item.getDouble("unit_price")
                            } catch (e: Exception) {
                                0.0
                            }

                            val totalAmount = try {
                                item.getDouble("total_amount")
                            } catch (e: Exception) {
                                0.0
                            }

                            products.add(Product(quantity, description, unitPrice, totalAmount))
                        }
                    }

                    onSuccess(supplier, products, date, total)

                } catch (e: Exception) {
                    onError("Error procesando respuesta: ${e.message}")
                }
            }
        })
    }
}
 */