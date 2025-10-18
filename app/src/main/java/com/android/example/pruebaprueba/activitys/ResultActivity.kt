package com.android.example.pruebaprueba.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.models.Product
import com.android.example.pruebaprueba.adapter.ProductAdapter
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.config.AppDatabase
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.Factura
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class ResultActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val supplier = intent.getStringExtra("supplier") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val total = intent.getDoubleExtra("total", 0.0)
        val products = intent.getParcelableArrayListExtra<Product>("products") ?: arrayListOf()

        val supplierEdit = findViewById<EditText>(R.id.supplierEdit)
        val totalEdit = findViewById<EditText>(R.id.totalEdit)
        val btnGuardad = findViewById<Button>(R.id.btnG)
        val recyclerView = findViewById<RecyclerView>(R.id.productsRecyclerView)

        db = AppDatabase.getDatabase(applicationContext)

        supplierEdit.setText(supplier)
        totalEdit.setText(total.toString())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductAdapter(products){
            val nuevoTotal = products.sumOf {
                it.total
            }
            totalEdit.setText(nuevoTotal.toString())
        }
      //  val UpdateTotalProductos = products.sumOf { it.total }//Actualizado
        //totalEdit.setText(UpdateTotalProductos.toString())
        btnGuardad.setOnClickListener {
            val UpdateSupplier = supplierEdit.text.toString()
            val UpdateTotal = totalEdit.text.toString().toDoubleOrNull() ?: 0.0
            val UpdateTotalProductos = products.sumOf { it.total }//Actualizado

            //totalEdit.text = UpdateTotalProductos
            val factura = Factura(0, "123", date, UpdateTotalProductos)
            CoroutineScope(Dispatchers.IO).launch {
                val facturaId = factura?.let { db.facturaDao().insertarFactura(it).toInt() }

                if(products!!.isNotEmpty() && facturaId != null){
                    for(ar in products){
                        val articulo = Articulo(0, facturaId, ar.description, ar.quantity, ar.unitPrice, ar.total)
                        Log.d("articulo", ar.toString())
                        db.articuloDao().insertarArticulo(articulo)
                    }

                    val intent = Intent(this@ResultActivity, FormularioPersonaActivity::class.java)
                    intent.putExtra("facturaId", facturaId)
                    startActivity(intent)
                }
            }

        }
    }
}