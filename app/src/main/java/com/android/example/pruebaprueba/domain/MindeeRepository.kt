package com.android.example.pruebaprueba.domain

import com.android.example.pruebaprueba.domain.models.InferenceState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MindeeRepository {

    fun enqueueImageAndObserve(
        file: File,
        modelId: String,
        webhookIds: List<String> = emptyList(),
        alias: String? = null,
    ): Flow<InferenceState>
}

