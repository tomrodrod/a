package com.android.example.pruebaprueba.data.models

import com.android.example.pruebaprueba.data.service.models.JobDTO

data class JobEntity(
    val id: String?,
    val status: String?,
    val pollingUrl: String?,
    val resultUrl: String?,
    val modelId: String?,
    val filename: String?,
    val alias: String?,
)

fun JobDTO.toData() = JobEntity(
    id = this.id,
    status = this.status,
    pollingUrl = this.polling_url,
    resultUrl = this.result_url,
    modelId = this.model_id,
    filename = this.filename,
    alias = this.alias,
)