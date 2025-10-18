package com.android.example.pruebaprueba.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.android.example.pruebaprueba.models.ArticuloPersona
import com.android.example.pruebaprueba.models.ArticuloPersonaConDetalles


@Dao
interface ArticuloPersonaDao {
    @Insert
    suspend fun insertarArticuloPersona(artPer: ArticuloPersona): Long
/*Probar despues*/
    @Query("SELECT * FROM articulo_persona WHERE articuloId = :articuloId")
    suspend fun obtenerArticuloPersonaPorArticuloId(articuloId: Int): List<ArticuloPersona> // <- FALTABA EL TIPO GENÉRICO

    @Query("SELECT * FROM articulo_persona WHERE personaId = :personaId")
    suspend fun obtenerArticuloPersonaPorPersonaId(personaId: Int): List<ArticuloPersona> // <- FALTABA EL TIPO GENÉRICO

    @Query("SELECT * FROM articulo_persona")
    suspend fun obtenerTodosArticuloPersona(): List<ArticuloPersona> // <- FALTABA EL TIPO GENÉRICO

    @Transaction
    @Query("SELECT * FROM articulo_persona")
    suspend fun getAllConDetalles(): List<ArticuloPersonaConDetalles>

    //Cuantos Articulos tiene asignada esta persona de un articulo especifico
    @Query("SELECT SUM(cantidadIndividual) FROM articulo_persona WHERE articuloId= :articuloId")
    suspend fun obtenerCantidadAsignadaPorArticulo(articuloId: Int): Int?

}

