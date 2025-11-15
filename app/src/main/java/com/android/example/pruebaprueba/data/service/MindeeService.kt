package com.android.example.pruebaprueba.data.service

import com.android.example.pruebaprueba.core.exception.Failure
import com.android.example.pruebaprueba.core.functional.Either
import com.android.example.pruebaprueba.core.platform.service.Request
import com.android.example.pruebaprueba.data.service.models.InferenceResponseDTO
import com.android.example.pruebaprueba.data.service.models.JobResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MindeeService @Inject constructor(
    private val api: MindeeApi,
    private val request: Request
) {

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
        alias: RequestBody? = null
    ): Either<Failure, JobResponseDTO> = request.launch(
        api.uploadDocument(
            modelId,
            file,
            url,
            fileBase64,
            rawText,
            polygon,
            confidence,
            rag,
            alias,
            webhookIds,
        )
    )

    suspend fun getJob(jobId: String): Either<Failure, JobResponseDTO> = request.launch(
        api.getJob(jobId)
    )

    suspend fun getInference(inferenceId: String): Either<Failure, InferenceResponseDTO> = request.launch(
        api.getInference(inferenceId)
    )
}
