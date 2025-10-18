package com.android.example.pruebaprueba.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable


@Entity(tableName = "personas")
data class Persona(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val telefono: String? = null,
    val email: String? = null
) : Serializable
