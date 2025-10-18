package com.android.example.pruebaprueba.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "articulos",
    foreignKeys = [ForeignKey(
        entity = Factura::class,
        parentColumns = ["id"],
        childColumns = ["facturaId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Articulo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val facturaId: Int,
    val descripcion: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subTotal: Double
): Serializable

data class FacturaConArticulos(
    @Embedded val factura: Factura,
    @Relation(
        parentColumn = "id",
        entityColumn = "facturaId"
    )
    val articulos: List<Articulo>
) : Serializable
