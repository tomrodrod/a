package com.android.example.pruebaprueba.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var quantity: Int,
    var description: String,
    var unitPrice: Double,
    var total: Double
) : Parcelable