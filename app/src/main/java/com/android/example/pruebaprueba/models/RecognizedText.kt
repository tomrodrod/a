package com.android.example.pruebaprueba.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recognized_texts")
data class RecognizedText(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
