package com.android.example.pruebaprueba.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.android.example.pruebaprueba.models.Factura
import com.android.example.pruebaprueba.models.FacturaConArticuloPersonaDetalles
import com.android.example.pruebaprueba.models.FacturaConArticulos

@Dao
interface FacturaDao {
    @Insert
    suspend fun insertarFactura(factura: Factura): Long

    @Query("SELECT * FROM facturas")
    suspend fun obtenerFacturas(): List<Factura> // <- FALTABA EL TIPO GENÉRICO

    @Transaction
    @Query("SELECT * FROM facturas")
    suspend fun obtenerFacturasconArticulos(): List<FacturaConArticulos> // <- FALTABA EL TIPO GENÉRICO


    @Transaction
    @Query("SELECT * FROM facturas")
    suspend fun obtenerFacturasCompleta(): List<FacturaConArticuloPersonaDetalles> // <- FALTABA EL TIPO GENÉRICO

}