package com.android.example.pruebaprueba.models


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable


// Entidades principales
@Entity(tableName = "facturas")
data class Factura(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val numeroFactura: String,
    val fecha: String,
    val total: Double
)

data class FacturaConArticuloPersonaDetalles(
    @Embedded val factura: Factura,

    @Relation(
        entity = ArticuloPersona::class,
        parentColumn = "id", // Factura.id
        entityColumn = "articuloId",
        associateBy = Junction(
            value = Articulo::class,
            parentColumn = "facturaId", // Articulo.facturaId = Factura.id
            entityColumn = "id"          // Articulo.id = ArticuloPersona.articuloId
        )
    )
    val detalles: List<ArticuloPersonaConDetalles>
)

