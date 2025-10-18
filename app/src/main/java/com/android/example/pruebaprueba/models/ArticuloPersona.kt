package com.android.example.pruebaprueba.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(
    tableName = "articulo_persona",
    foreignKeys = [
        ForeignKey(
            entity = Articulo::class,
            parentColumns = ["id"],
            childColumns = ["articuloId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Persona::class,
            parentColumns = ["id"],
            childColumns = ["personaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ArticuloPersona(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val articuloId: Int,
    val personaId: Int,
    val cantidadIndividual: Int,
    val subTotalIndividual: Double
): Serializable

//2     //2

data class ArticuloPersonaConDetalles(
    @Embedded val relacion: ArticuloPersona,

    @Relation(
        parentColumn = "articuloId",
        entityColumn = "id"
    )
    val articulo: Articulo,

    @Relation(
        parentColumn = "personaId",
        entityColumn = "id"
    )
    val persona: Persona
): Serializable
