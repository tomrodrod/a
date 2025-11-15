package com.android.example.pruebaprueba.domain.models

import com.android.example.pruebaprueba.core.exception.Failure

sealed interface InferenceState {
    data object Uploading : InferenceState
    data class Enqueued(val jobId: String) : InferenceState
    data object Processing : InferenceState
    data class Completed<T>(val data: T) : InferenceState
    data class Error(val failure: Failure) : InferenceState
}