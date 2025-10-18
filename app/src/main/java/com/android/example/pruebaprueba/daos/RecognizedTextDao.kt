package com.android.example.pruebaprueba.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.example.pruebaprueba.models.RecognizedText


//Pendiente
@Dao
interface RecognizedTextDao {
    @Insert
    suspend fun insert(text: RecognizedText)

    @Query("SELECT * FROM recognized_texts ORDER BY timestamp DESC")
    suspend fun getAll(): List<RecognizedText>
}