package com.android.example.pruebaprueba.data.models

import com.android.example.pruebaprueba.data.service.models.InferenceDTO

data class InferenceEntity(
    val id: String?,
    val modelId: String?,
)

fun InferenceDTO.toData() = InferenceEntity(
    id = this.id,
    modelId = this.model_id,
)