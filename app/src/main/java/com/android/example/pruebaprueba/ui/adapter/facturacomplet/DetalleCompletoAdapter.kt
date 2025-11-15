package com.android.example.pruebaprueba.ui.adapter.facturacomplet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.models.Articulo

/*
class DetalleCompletoAdapter(
    private val articulos: List<ArticuloConPersona>,
    private val onClick:(ArticuloConPersona)-> Unit
): RecyclerView.Adapter<DetalleCompletoAdapter.DetalleViewHolder>() {

    inner class DetalleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val descripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val cantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        val subTotal: TextView = itemView.findViewById(R.id.txtSubtotal)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetalleCompletoAdapter.DetalleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articulo, parent, false)

        return DetalleViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetalleCompletoAdapter.DetalleViewHolder, position: Int) {
        val articulo = articulos[position]
        holder.descripcion.text = articulo.articulo.descripcion
        holder.subTotal.text = articulo.articulo.subTotal.toString()
        holder.precio.text = articulo.articulo.precioUnitario.toString()
        holder.cantidad.text = articulo.articulo.cantidad.toString()

        holder.itemView.setOnClickListener {
            onClick(articulo)
        }
    }

    override fun getItemCount(): Int {
        return articulos.size
    }
}
 */