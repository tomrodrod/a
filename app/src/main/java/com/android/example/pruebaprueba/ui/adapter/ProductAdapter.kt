package com.android.example.pruebaprueba.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pruebaprueba.models.Product
import com.android.example.pruebaprueba.R

class ProductAdapter(private val products: List<Product>,
    private val onProductChange: () -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){

        val quantityEdit: EditText = itemView.findViewById<EditText>(R.id.quantityEdit)
        val descriptionEdit: EditText = itemView.findViewById<EditText>(R.id.descriptionEdit)
        val unitPriceEdit: EditText = itemView.findViewById<EditText>(R.id.unitPriceEdit)
        val totalEdit: EditText = itemView.findViewById<EditText>(R.id.totalEdit)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_editable, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = products[position]

        holder.quantityEdit.setText(item.quantity.toString())
        holder.descriptionEdit.setText(item.description.toString())
        holder.unitPriceEdit.setText(item.unitPrice.toString())
        holder.totalEdit.setText(item.total.toString())
        //Actualizar en caso de que los editen

        holder.quantityEdit.addTextChangedListener{
            val cantidad = it.toString()
            products[position].quantity = cantidad.toIntOrNull() ?: 0
            products[position].total =  products[position].quantity * products[position].unitPrice
            holder.totalEdit.setText(item.total.toString())
            onProductChange()
        }

        holder.descriptionEdit.addTextChangedListener{
            products[position].description = it.toString()
        }
        holder.unitPriceEdit.addTextChangedListener{
            products[position].unitPrice = it.toString().toDoubleOrNull() ?: 0.0
            products[position].total =  products[position].quantity * products[position].unitPrice
            holder.totalEdit.setText(item.total.toString())
            onProductChange()
        }
        holder.totalEdit.addTextChangedListener{
            products[position].total = it.toString().toDoubleOrNull() ?: 0.0
        }
    }

    override fun getItemCount(): Int = products.size
}