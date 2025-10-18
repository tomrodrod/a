package com.android.example.pruebaprueba.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.example.pruebaprueba.models.Articulo

@Dao
interface ArticuloDao {
    @Insert
    suspend fun insertarArticulo(articulo: Articulo): Long

    @Query("SELECT * FROM articulos")
    suspend fun obtenerArticulos(): List<Articulo> // <- FALTABA EL TIPO GENÉRICO

    @Query("SELECT * FROM articulos WHERE facturaId = :idFactura")
    suspend fun obtenerArticulosPorId(idFactura: Int): List<Articulo>

    @Query("SELECT * FROM articulos WHERE facturaId = :facturaId")
    suspend fun obtenerArticulosPorFacturaId(facturaId: Int): List<Articulo> // <- FALTABA EL TIPO GENÉRICO

    @Query("SELECT * FROM articulos WHERE facturaId = :facId LIMIT 1")
    suspend fun obtenerArticuloPorId(facId: Int): Articulo?

}