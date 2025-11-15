package com.android.example.pruebaprueba.data.datasource

import com.android.example.pruebaprueba.domain.models.InferenceState
import kotlinx.coroutines.flow.Flow

interface WebhookEventDataSource {
    fun observeJob(jobId: String): Flow<InferenceState>
}
