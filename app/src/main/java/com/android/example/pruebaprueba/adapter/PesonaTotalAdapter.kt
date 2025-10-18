package com.android.example.pruebaprueba.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles
import com.android.example.pruebaprueba.models.PersonaConTotal


class PersonaTotalAdapter(
    private val personas: List<PersonaConTotal>,  private val onClick: (PersonaConTotal)-> Unit
): RecyclerView.Adapter<PersonaTotalAdapter.PersonaTotalViewHolder>() {

    inner class PersonaTotalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreCompleto: TextView = itemView.findViewById(R.id.txtNombreUsuario)
        val subTotal: TextView = itemView.findViewById(R.id.txtSubTotalIndividual)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonaTotalAdapter.PersonaTotalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_persona, parent, false)

        return PersonaTotalViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaTotalAdapter.PersonaTotalViewHolder, position: Int) {
        val persona = personas[position]
        holder.nombreCompleto.text = "Nombre: ${persona.nombre}"
        holder.subTotal.text = "SubTotal Indivisual: ${persona.total}"

        holder.itemView.setOnClickListener {
            onClick(persona)
        }

    }

    override fun getItemCount(): Int {
        return personas.size
    }
}