package com.android.example.pruebaprueba.models

import java.io.Serializable


data class ArticuloConCantidad(
    val articulo: Articulo,
    val costo: Double,
    val cantidad: Int
) : java.io.Serializable


data class PersonaConTotal(
    val personaId: Int,
    val nombre: String,
    val total: Double,
    val articulos: List<ArticuloConCantidad>
) : Serializable