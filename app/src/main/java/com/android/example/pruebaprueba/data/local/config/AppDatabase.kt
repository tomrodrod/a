package com.android.example.pruebaprueba.data.local.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.example.pruebaprueba.data.local.daos.ArticuloDao
import com.android.example.pruebaprueba.data.local.daos.ArticuloPersonaDao
import com.android.example.pruebaprueba.data.local.daos.FacturaDao
import com.android.example.pruebaprueba.data.local.daos.PersonaDao
import com.android.example.pruebaprueba.data.local.daos.RecognizedTextDao
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.Factura
import com.android.example.pruebaprueba.models.Persona
import com.android.example.pruebaprueba.models.ArticuloPersona
import com.android.example.pruebaprueba.models.RecognizedText

@Database(entities = [RecognizedText::class,
                        Factura::class,
                        Articulo::class,
                        Persona::class,
                        ArticuloPersona::class], version = 10, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recognizedTextDao(): RecognizedTextDao
    abstract fun facturaDao(): FacturaDao
    abstract fun articuloDao(): ArticuloDao
    abstract fun personaDao(): PersonaDao
    abstract fun articulo_persona(): ArticuloPersonaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "texts-db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}