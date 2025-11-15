package com.android.example.pruebaprueba.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.ui.activitys.FormularioPersonaActivity
import com.android.example.pruebaprueba.ui.activitys.ResultActivity
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles
import com.android.example.pruebaprueba.models.FacturaConArticulos

class DetalleAdapter(
    private val context: Context,
    private val facturaId: Int,
    private val articulos: List<ArticuloPersonaConDetalles>,
    private val onClick:(ArticuloPersonaConDetalles)-> Unit,
    //private val onClickAgregar(ArticuloPersonaConDetalles) -> Unit
): RecyclerView.Adapter<DetalleAdapter.DetalleViewHolder>() {

    inner class DetalleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val descripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val cantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        val subTotal: TextView = itemView.findViewById(R.id.txtSubtotal)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)
        val boton: Button = itemView.findViewById(R.id.btnAgregarPersona)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetalleAdapter.DetalleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articulo, parent, false)

        return DetalleViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetalleAdapter.DetalleViewHolder, position: Int) {
        val articulo = articulos[position]
        holder.descripcion.text = articulo.articulo.descripcion
        holder.subTotal.text = articulo.articulo.subTotal.toString()
        holder.precio.text = articulo.articulo.precioUnitario.toString()
        holder.cantidad.text = articulo.articulo.cantidad.toString()

        holder.itemView.setOnClickListener {
            onClick(articulo)
        }

        holder.boton.setOnClickListener {
            val intent = Intent(context, FormularioPersonaActivity::class.java)
           // intent.putExtra("articulo", articulo)
            intent.putExtra("facturaId", facturaId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articulos.size
    }
}