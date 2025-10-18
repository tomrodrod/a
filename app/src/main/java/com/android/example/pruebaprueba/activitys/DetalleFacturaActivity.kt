package com.android.example.pruebaprueba.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.activitys.HistorialFacturasActivity
import com.android.example.pruebaprueba.adapter.DetalleAdapter
import com.android.example.pruebaprueba.adapter.FacturaAdapter
import com.android.example.pruebaprueba.adapter.PersonaTotalAdapter
import com.android.example.pruebaprueba.config.AppDatabase
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles
import com.android.example.pruebaprueba.repositorys.calcularConsumoPorPersona
import kotlinx.coroutines.launch
import java.io.Serializable

class DetalleFacturaActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_factura)

        //val listaArticulos = intent.getSerializableExtra("articulos") as ArrayList<ArticuloPersonaConDetalles>
        val facturaId = intent.getIntExtra("facturaId", -1)//Total de la factura mandar tambien

        //val agruparPorIdArticulo = listaArticulos.groupBy { it.articulo.facturaId }
        //val listaArticulos = intent.getSerializableExtra("articulos") as ArrayList<ArticuloConPersona>
        recycler = findViewById(R.id.articulos)
      //  Log.d("ARTICULOS EN DETALLE", listaArticulos.toString())
        recycler.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            val listaConsumoPersona = calcularConsumoPorPersona(facturaId, db)

            recycler.adapter = PersonaTotalAdapter(listaConsumoPersona) { personaConTotal ->
                Log.d("Adapter de Detalle", listaConsumoPersona.toString())
                val intent = Intent(this@DetalleFacturaActivity, PersonasActivity::class.java)
                intent.putExtra("persona", personaConTotal.nombre)
                intent.putExtra("articulos", personaConTotal.articulos as Serializable)
                intent.putExtra("total", personaConTotal.total)
                intent.putExtra("facturaId", facturaId)
                startActivity(intent)
//Modal
            }
        }



    }
}