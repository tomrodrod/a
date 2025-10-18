package com.android.example.pruebaprueba

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.example.pruebaprueba.activitys.CameraActivity
import com.android.example.pruebaprueba.activitys.HistorialFacturasActivity
import com.android.example.pruebaprueba.config.AppDatabase
import com.android.example.pruebaprueba.databinding.ActivityMainBinding
import com.android.example.pruebaprueba.mindeeocr.MindeeService
import android.os.Handler
import android.os.Looper
import com.android.example.pruebaprueba.mindee.ListaProductos
import com.android.example.pruebaprueba.mindee.pollRepetir
import com.android.example.pruebaprueba.models.Product

import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)
       // testDatabase(db)

        binding.buttonStartCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
            //procesarFactura()
        }

        binding.btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialFacturasActivity::class.java))
            //procesarFactura()
        }

    }



}
/*
class MainActivity : AppCompatActivity() {

    private lateinit var service: MindeeService

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        service = MindeeService("md_04pbXNNc72Xr3O8jNB8ssYO856XVS0zM")

        // 1. Obtener el drawable como InputStream
        val inputStream = resources.openRawResource(R.drawable.test_receipt)

        // 2. Copiar el recurso a un archivo temporal en cacheDir
        val imageFile = File(cacheDir, "test_receipt.jpg")
        inputStream.use { input ->
            imageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

    // Paso 1: hacer enqueue POST
        service.sendRequest(imageFile, "a7338906-e91a-471b-90ec-acd55e84daf4") { jobResponse, error ->
            runOnUiThread {
                if (error != null) {
                    Log.d("PRUEBA", "Error enqueue: ${error.message}")
                } else if (jobResponse != null) {
                    Log.d("PRUEBA", "Job ID: ${jobResponse.job.id}")
                    Log.d("PRUEBA", "Polling URL: ${jobResponse.job.pollingUrl}")
                    Log.d("PRUEBA", "Estado inicial: ${jobResponse.job.status}")
                    Handler(Looper.getMainLooper()).postDelayed({

                      pollRepetir(jobResponse.job.pollingUrl) {
    Log.d("PRUEBA", "Productos cargados: ${ListaProductos.products.size}")
    // Actualizas la UI aquí
}

                      //  Log.d("PRODUCTOS", "${productos}")
                    },3000)


                }
            }
        }
    }



}

 */




