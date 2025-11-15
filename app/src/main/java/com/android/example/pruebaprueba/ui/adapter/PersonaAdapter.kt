package com.android.example.pruebaprueba.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles


class PersonaAdapter(
    private val personas: List<ArticuloPersonaConDetalles>
): RecyclerView.Adapter<PersonaAdapter.DetalleViewHolder>() {

    inner class DetalleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreCompleto: TextView = itemView.findViewById(R.id.txtNombreUsuario)
        val correo: TextView = itemView.findViewById(R.id.txtEmail)
        val cantidad: TextView = itemView.findViewById(R.id.txtCantidadIndividual)
        val subTotal: TextView = itemView.findViewById(R.id.txtSubTotalIndividual)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonaAdapter.DetalleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_persona, parent, false)

        return DetalleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaAdapter.DetalleViewHolder, position: Int) {
        val persona = personas[position]
        holder.nombreCompleto.text = "Nombre: ${persona.persona.nombre} ${persona.persona.apellido}"
        holder.correo.text = "Correo: ${persona.persona.email}"
        holder.cantidad.text = "Cantidad Indivisual: ${persona.relacion.cantidadIndividual}"
        holder.subTotal.text = "SubTotal Indivisual: ${persona.relacion.subTotalIndividual}"

    }

    override fun getItemCount(): Int {
        return personas.size
    }
}