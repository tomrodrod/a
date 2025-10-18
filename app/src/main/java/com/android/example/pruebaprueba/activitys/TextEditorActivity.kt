package com.android.example.pruebaprueba.activitys
/*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pruebaprueba.config.AppDatabase
import com.android.example.pruebaprueba.config.FacturasService
import com.android.example.pruebaprueba.models.RecognizedText
import com.android.example.pruebaprueba.databinding.ActivityTextEditorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TextEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recognizedText = intent.getStringExtra("RECOGNIZED_TEXT")
        binding.editText.setText(recognizedText)



        binding.buttonSave.setOnClickListener {

            val editedText = binding.editText.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(this@TextEditorActivity)
                //db.recognizedTextDao().insert(RecognizedText(content = editedText))
                val servicio = FacturasService(db)
                servicio.procesarTextoReconocido(RecognizedText(content = editedText))

                runOnUiThread {
                    Toast.makeText(this@TextEditorActivity, "Texto guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad si quieres volver atrás
                }
            }
        }
    }
}

 */
