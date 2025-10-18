package com.android.example.pruebaprueba.activitys

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.example.pruebaprueba.MainActivity
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.config.AppDatabase
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.ArticuloPersona
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles
import com.android.example.pruebaprueba.models.Persona
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FormularioPersonaActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private var articulos: List<Articulo> = emptyList()
    private var personas: List<Persona> = emptyList()

    private lateinit var spinnerPersona: Spinner
    private lateinit var spinnerArticulo: Spinner
    private lateinit var etCantidad: EditText
    private lateinit var btnAgregarPersona: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnOmitir: Button

    private var personaId: Long? = null
    private var articuloId: Int = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_formulario_persona)

        val facturaId = intent.getIntExtra("facturaId", -1)
        //val articulo = intent.getSerializableExtra("articulo") as ArticuloPersonaConDetalles

        db = AppDatabase.getDatabase(applicationContext)

        spinnerPersona = findViewById(R.id.spPersona)
        spinnerArticulo = findViewById(R.id.spArticulos)

/*
        if(articulo != null){
            articuloId = articulo.articulo.id
            spinnerArticulo.visibility = View.GONE
        }
        */
        etCantidad = findViewById(R.id.txtCantidadFormulario)

        btnAgregarPersona = findViewById(R.id.btnAgregar)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnOmitir = findViewById(R.id.btnOmitir)

        cargarSpinner(facturaId,/* articulo.articulo.id*/)

        btnAgregarPersona.setOnClickListener {
            mostrarDialogPersona(facturaId)
        }

        btnGuardar.setOnClickListener {
            guardarArticuloPersona(personaId)
        }


    }

    private fun cargarSpinner(facturaId:Int /*, articuloId:Int*/){
        lifecycleScope.launch {
            personas = db.personaDao().obtenerPersonas()
            spinnerPersona.adapter = ArrayAdapter(this@FormularioPersonaActivity,
                android.R.layout.simple_spinner_dropdown_item,
                personas.map {it.nombre + " "+ it.apellido})
          /* if(articuloId == 0){
             //AQUI PONER EL SPINNER DE ARTICULO
           }
                     */
            articulos = db.articuloDao().obtenerArticulosPorId(facturaId) as List<Articulo>
            spinnerArticulo.adapter = ArrayAdapter(this@FormularioPersonaActivity,
                android.R.layout.simple_spinner_dropdown_item,
                articulos.map {it.descripcion})
        }
    }


    private fun mostrarDialogPersona(facturaId:Int): Long?{
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val inputNombre = EditText(this)
        inputNombre.hint = "Ingresa nombre de la persona"

        layout.addView(inputNombre)
        val inputApellido = EditText(this)
        inputApellido.hint = "Ingresa apellido de la persona"
        layout.addView(inputApellido)

        AlertDialog.Builder(this)
            .setTitle("Agregar Persona nueva")
            .setView(layout)
            .setPositiveButton("Guardar") { _,_ ->
                val nombre = inputNombre.text.toString()
                val apellido = inputApellido.text.toString()

                if(nombre.isNotBlank() && apellido.isNotBlank()){
                    lifecycleScope.launch {
                        val nueva = Persona(0, nombre, apellido)
                        personaId = db.personaDao().insertarPersona(nueva)
                        Log.d("FORMULARIO", personaId.toString())
                    }
                }

                cargarSpinner(facturaId, /*articuloId*/)

            }
            .setNegativeButton("Cancelar", null)
            .show()

        return personaId
    }


    private fun guardarArticuloPersona(personaId: Long?){
        val personaseleccionada = personas.getOrNull(spinnerPersona.selectedItemPosition)
        val articuloeleccionada = articulos.getOrNull(spinnerArticulo.selectedItemPosition)
        val cantidad = etCantidad.text.toString().toIntOrNull()//3
        val precioUnitario = articuloeleccionada?.precioUnitario
        var subTotal = 0.0
       // val articuloeleccionada = articulos.getOrNull(spinnerArticulo.selectedItemPosition)

        if (cantidad != null && precioUnitario != null) {
                subTotal = cantidad * precioUnitario
            if(cantidad > articuloeleccionada!!.cantidad){
                Toast.makeText(this, "Cantidad supera lo disponible", Toast.LENGTH_SHORT).show()
                return
            }
            //cantidad  (articuloeleccionada?.precioUnitario ?: 0.0)
            // cantidad * articuloeleccionada!!.precioUnitario
            if (articuloeleccionada == null) {
                Toast.makeText(this, "No hay artículo seleccionado", Toast.LENGTH_SHORT).show()
                return
            }else{
                Log.d("ArticuloSeleccionado", "${articuloeleccionada}")
                }


            lifecycleScope.launch {
                val cantidadAsignadaEnBD = db.articulo_persona().obtenerCantidadAsignadaPorArticulo(articuloeleccionada.id) ?: 0
                Log.d("cantidadAsignadaEnBD", "${cantidadAsignadaEnBD}")

                val cantidadDisponible = articuloeleccionada.cantidad - cantidadAsignadaEnBD
                if(cantidad > cantidadDisponible){
                    Toast.makeText(this@FormularioPersonaActivity, "Solo hay ${cantidadDisponible} para este producto", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                //cambiar total
                subTotal = cantidad * articuloeleccionada.precioUnitario
                val articuloPersona = when {
                    personaId != null -> ArticuloPersona(0, articuloeleccionada!!.id, personaId.toInt(), cantidad, subTotal)
                    personaseleccionada != null -> ArticuloPersona(0, articuloeleccionada!!.id, personaseleccionada.id, cantidad, subTotal)
                    else -> null
                }

                if(articuloPersona == null){
                    Toast.makeText(this@FormularioPersonaActivity, "Persona Null", Toast.LENGTH_SHORT).show()
                    return@launch

                }



                val idAP = db.articulo_persona().insertarArticuloPersona(articuloPersona)
                if(idAP > 0){
                    Toast.makeText(this@FormularioPersonaActivity, "Se inserto " + idAP , Toast.LENGTH_SHORT).show()
                    mostrarRepetirPersona()
                    //Repetir persona Dialog

                }else{
                    Toast.makeText(this@FormularioPersonaActivity, "Error al insertar" , Toast.LENGTH_SHORT).show()

                }
            }
        }



    }


    private fun mostrarRepetirPersona(){
        AlertDialog.Builder(this)
            .setTitle("Desea agregar otra persona?")
            .setMessage("Puedes seguir agg mas personas o finalizar el registro?")
            .setPositiveButton("Ingresar otra persona"){ _,_ ->
                limpiarFormulario()
            }
            .setNegativeButton("Omitar") {_,_->
                finish()//cerrar la pantalla actual
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }.show()

    }

    private fun limpiarFormulario(){
        etCantidad.text.clear()
        spinnerPersona.setSelection(0)
        spinnerArticulo.setSelection(0)
    }

}