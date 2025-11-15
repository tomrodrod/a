package com.android.example.pruebaprueba.models.mindee

data class MindeeJobResponse(
    val job: MindeeJob
)

data class MindeeJob(
    val id: String,
    val filename: String,
    val alias: String?,
    val status: String,
    val pollingUrl: String,
    val webhooks: List<String>,
    val error: String?
)
