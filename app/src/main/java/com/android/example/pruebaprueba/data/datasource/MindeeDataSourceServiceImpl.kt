package com.android.example.pruebaprueba.data.datasource

import com.android.example.pruebaprueba.core.exception.Failure
import com.android.example.pruebaprueba.core.extensions.flatMap
import com.android.example.pruebaprueba.core.functional.Either
import com.android.example.pruebaprueba.data.models.InferenceResponseEntity
import com.android.example.pruebaprueba.data.models.JobResponseEntity
import com.android.example.pruebaprueba.data.models.toData
import com.android.example.pruebaprueba.data.service.MindeeService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MindeeDataSourceServiceImpl @Inject constructor(
    private val service: MindeeService
) : MindeeDataSourceService {

    override suspend fun enqueue(
        modelId: RequestBody,
        file: MultipartBody.Part?,
        url: RequestBody?,
        fileBase64: RequestBody?,
        webhookIds: List<MultipartBody.Part>?,
        rawText: RequestBody?,
        polygon: RequestBody?,
        confidence: RequestBody?,
        rag: RequestBody?,
        alias: RequestBody?
    ): Either<Failure, JobResponseEntity> {

        return service.enqueue(
            modelId = modelId,
            file = file,
            url = url,
            fileBase64 = fileBase64,
            webhookIds = webhookIds,
            rawText = rawText,
            polygon = polygon,
            confidence = confidence,
            rag = rag,
            alias = alias
        ).flatMap { jobResponseDTO ->
            Either.Right(jobResponseDTO.toData())
        }
    }

    override suspend fun getJob(jobId: String): Either<Failure, JobResponseEntity> {
        return service.getJob(jobId).flatMap { jobResponseDTO ->
            Either.Right(jobResponseDTO.toData())
        }
    }

    override suspend fun getInference(inferenceId: String): Either<Failure, InferenceResponseEntity> {
        return service.getInference(inferenceId).flatMap { inferenceResponseDTO ->
            Either.Right(inferenceResponseDTO.toData())
        }
    }

}