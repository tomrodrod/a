package com.android.example.pruebaprueba.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.adapter.FacturaAdapter
import com.android.example.pruebaprueba.config.AppDatabase
import kotlinx.coroutines.launch
import java.io.Serializable

class HistorialFacturasActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_facturas)
        recycler = findViewById(R.id.historial)

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.visibility = View.GONE

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val facturas = db.facturaDao().obtenerFacturasconArticulos()
            val facturasCompleta = db.facturaDao().obtenerFacturasCompleta()
            Log.d("Facturas", facturasCompleta.toString())

            val facturasCompletaFiltradas = facturasCompleta.filter { it?.factura?.total != 0.0}

            recycler.adapter = FacturaAdapter(facturasCompletaFiltradas) { fact ->
                val intent = Intent(this@HistorialFacturasActivity, DetalleFacturaActivity::class.java)
                //intent.putExtra("articulos", fact.detalles as Serializable)
                intent.putExtra("facturaId", fact.factura.id)
                startActivity(intent)
            }

/*


            recycler.adapter = FacturaCompletaAdapter(facturasCompletaFiltradas as List<FacturaCompleta>) { fact ->
                val intent = Intent(this@HistorialFacturasActivity, DetalleFacturaActivity::class.java)
                intent.putExtra("articulos", fact.articulos as Serializable)
                startActivity(intent)
            }

            */

            recycler.visibility = View.VISIBLE
        }


    }
}