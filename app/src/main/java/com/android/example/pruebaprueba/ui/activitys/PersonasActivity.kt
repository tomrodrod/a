package com.android.example.pruebaprueba.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.ui.adapter.DetalleAdapter
import com.android.example.pruebaprueba.ui.adapter.PersonaAdapter
import com.android.example.pruebaprueba.ui.adapter.facturacomplet.ArticuloAdapter
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.ArticuloConCantidad
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles
import com.android.example.pruebaprueba.models.Persona
import java.io.Serializable

class PersonasActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personas)
        val nombrePersona = intent.getStringExtra("persona")
        val totalPersona = intent.getDoubleExtra("total", 0.0)//Total de la factura mandar tambien
        val facturaId = intent.getIntExtra("facturaId", -1)//Total de la factura mandar tambien

        val articulos = intent.getSerializableExtra("articulos") as List<ArticuloConCantidad>

        recycler = findViewById(R.id.recyclePersonas)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ArticuloAdapter(this, facturaId,   articulos){articulo ->}


       //
    }
}