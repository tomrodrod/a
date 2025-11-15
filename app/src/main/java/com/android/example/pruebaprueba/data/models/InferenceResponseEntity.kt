package com.android.example.pruebaprueba.data.models

import com.android.example.pruebaprueba.data.service.models.InferenceResponseDTO

data class InferenceResponseEntity(
    val inference: InferenceEntity?
)

fun InferenceResponseDTO.toData() = InferenceResponseEntity(
    inference = this.inference?.toData()
)