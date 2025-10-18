package com.android.example.pruebaprueba.adapter.facturacomplet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.activitys.FormularioPersonaActivity
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.ArticuloConCantidad
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles


class ArticuloAdapter(
    private val context: Context,
    private val facturaId: Int,
    private val articulos: List<ArticuloConCantidad>,
    private val onClick:(ArticuloConCantidad)-> Unit,//añadir nueva persona
    //private val onClickAgregar(ArticuloPersonaConDetalles) -> Unit
): RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder>() {

    inner class ArticuloViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
      //  val txtNombrePersona: TextView = itemView.findViewById(R.id.txtNombrePersona)
        val descripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val cantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        val subTotal: TextView = itemView.findViewById(R.id.txtSubtotal)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)
        val boton: Button = itemView.findViewById(R.id.btnAgregarPersona)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticuloAdapter.ArticuloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articulo, parent, false)

        return ArticuloViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticuloAdapter.ArticuloViewHolder, position: Int) {
        val articulo = articulos[position]
      //  holder.txtNombrePersona.text = nombrePersona
        holder.descripcion.text = articulo.articulo.descripcion
        holder.subTotal.text = articulo.costo.toString()
        holder.precio.text =  articulo.articulo.precioUnitario.toString()
        holder.cantidad.text = articulo.cantidad.toString()

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