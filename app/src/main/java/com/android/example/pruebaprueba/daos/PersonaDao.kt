package com.android.example.pruebaprueba.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.example.pruebaprueba.models.Persona

@Dao
interface PersonaDao {
    @Insert
    suspend fun insertarPersona(persona: Persona): Long

    @Query("SELECT * FROM personas")
    suspend fun obtenerPersonas(): List<Persona>

    @Query("SELECT * FROM personas WHERE id = :personaId LIMIT 1")
    suspend fun obtenerPersonaPorId(personaId: Int): Persona?
}
