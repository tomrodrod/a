package com.android.example.pruebaprueba.adapter.facturacomplet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R

/*
class FacturaCompletaAdapter(
    private val facturas: List<FacturaCompleta>,
    private val onClick:(FacturaCompleta)-> Unit
): RecyclerView.Adapter<FacturaCompletaAdapter.FacturaViewHolder>() {

    inner class FacturaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //val numeroFactura: TextView = itemView.findViewById(R.id.txtCantidad)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacturaCompletaAdapter.FacturaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_factura, parent, false)

        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaCompletaAdapter.FacturaViewHolder, position: Int) {
        val factura = facturas[position]
        Log.d("ADAPTER", factura.toString())
        holder.txtFecha.text = factura.factura.fecha
        holder.txtTotal.text = factura.factura.total.toString()
        holder.txtCantidad.text = factura.articulos.size.toString()

        holder.itemView.setOnClickListener {
            onClick(factura)
        }
    }

    override fun getItemCount(): Int {
        return facturas.size
    }
}
 */