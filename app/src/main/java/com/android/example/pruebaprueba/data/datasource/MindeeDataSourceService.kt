package com.android.example.pruebaprueba.data.datasource

import com.android.example.pruebaprueba.core.exception.Failure
import com.android.example.pruebaprueba.core.functional.Either
import com.android.example.pruebaprueba.data.models.InferenceResponseEntity
import com.android.example.pruebaprueba.data.models.JobResponseEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MindeeDataSourceService {

    suspend fun enqueue(
        modelId: RequestBody,
        file: MultipartBody.Part? = null,
        url: RequestBody? = null,
        fileBase64: RequestBody? = null,
        webhookIds: List<MultipartBody.Part>? = null,
        rawText: RequestBody? = null,
        polygon: RequestBody? = null,
        confidence: RequestBody? = null,
        rag: RequestBody? = null,
        alias: RequestBody? = null,
    ): Either<Failure, JobResponseEntity>

    suspend fun getJob(jobId: String): Either<Failure, JobResponseEntity>
    suspend fun getInference(inferenceId: String): Either<Failure, InferenceResponseEntity>
}