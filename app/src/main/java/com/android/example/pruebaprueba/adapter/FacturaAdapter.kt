package com.android.example.pruebaprueba.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.models.Factura
import com.android.example.pruebaprueba.models.FacturaConArticuloPersonaDetalles
import com.android.example.pruebaprueba.models.FacturaConArticulos

class FacturaAdapter(
    private val facturas: List<FacturaConArticuloPersonaDetalles>,
    private val onClick:(FacturaConArticuloPersonaDetalles)-> Unit
    ): RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    inner class FacturaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //val numeroFactura: TextView = itemView.findViewById(R.id.txtCantidad)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacturaAdapter.FacturaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_factura, parent, false)

        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaAdapter.FacturaViewHolder, position: Int) {
       val factura = facturas[position]
        Log.d("ADAPTER", factura.toString())
        holder.txtFecha.text = factura.factura.fecha
        holder.txtTotal.text = factura.factura.total.toString()
        holder.txtCantidad.text = factura.detalles.size.toString()//relacion

        holder.itemView.setOnClickListener {
            onClick(factura)
        }
    }

    override fun getItemCount(): Int {
        return facturas.size
    }
}