package com.android.example.pruebaprueba.models.mindee

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MindeeJobResponse(
    val job: MindeeJob
)

@JsonClass(generateAdapter = true)
data class MindeeJob(
    val id: String,
    @Json(name = "model_id") val modelId: String,
    val filename: String,
    val alias: String?,
    @Json(name = "created_at") val createdAt: String,
    val status: String,
    @Json(name = "polling_url") val pollingUrl: String,
    @Json(name = "result_url") val resultUrl: String?,
    val webhooks: List<String>,
    val error: String?
)
