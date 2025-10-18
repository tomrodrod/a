package com.android.example.pruebaprueba.mindee

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.android.example.pruebaprueba.mindeeocr.MindeeService
import com.android.example.pruebaprueba.models.Product

//Singleton
object ListaProductos{
    //val products = mutableListOf<Product>()
    val products: MutableList<Product> = mutableListOf()
}

fun pollRepetir(pollingUrl: String){
    var service = MindeeService("md_04pbXNNc72Xr3O8jNB8ssYO856XVS0zM")


    service.JobStatusResponse(pollingUrl){jobResponse, error ->
          if (error != null) {
                Log.d("PRUEBA", "NO HAY RESPUESTA DESDE EL ERROR")
                Log.d("PRUEBA", "NO HAY RESPUESTA")
                Handler(Looper.getMainLooper()).postDelayed({
                    pollRepetir(pollingUrl)
                },2000)
                Log.d("PRUEBA", "Error JOB: ${error.message}")
            } else if (jobResponse != null) {//inferenci
                //Recorrer lo que es items
                jobResponse.inference.result.fields.lineItems?.items?.forEachIndexed { index, product ->
                    val descr = product.fields?.description?.value
                    val quantity = product.fields?.quantity?.value ?: 0
                    val unitPrice = product.fields?.unit_price?.value ?: 0.0
                    val total = product.fields?.total_price?.value ?: 0.0

                    val productoFinal= Product(quantity,descr.toString(),unitPrice,total)
                    ListaProductos.products.add(productoFinal)
                }


            }else{
                Log.d("PRUEBA", "NO HAY RESPUESTA")
                Handler(Looper.getMainLooper()).postDelayed({
                    pollRepetir(pollingUrl)
                },2000)

            }
        }
    //return products
}


fun pollRepetir(pollingUrl: String, onFinish: ((String?, String?, Double?) -> Unit)? = null) {
    val service = MindeeService("md_318K4ux4ihiAtMiEm5pid9vR8pdllGQn")

    service.JobStatusResponse(pollingUrl) { jobResponse, error ->
        if (error != null) {
            Log.d("PRUEBA", "Error: ${error.message}")
            Handler(Looper.getMainLooper()).postDelayed({
                pollRepetir(pollingUrl, onFinish)
            }, 2000)
        } else if (jobResponse != null) {
            val supplier = jobResponse.inference.result.fields.supplierName?.value ?: "Desconocido"
            val date = jobResponse.inference.result.fields.date?.value ?: "Sin fecha"
            val totalAmount = jobResponse.inference.result.fields.totalAmount?.value ?: 0.0

            jobResponse.inference.result.fields.lineItems?.items?.forEach { product ->
                val descr = product.fields?.description?.value ?: ""
                val quantity = product.fields?.quantity?.value ?: 0
                val unitPrice = product.fields?.unit_price?.value ?: 0.0
                val total = product.fields?.total_price?.value ?: 0.0

                ListaProductos.products.add(Product(quantity, descr, unitPrice, total))
            }

            onFinish?.invoke(supplier, date, totalAmount)
        }
    }
}
