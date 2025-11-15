package com.android.example.pruebaprueba.data.service

import com.android.example.pruebaprueba.data.service.models.InferenceResponseDTO
import com.android.example.pruebaprueba.data.service.models.JobDTO
import com.android.example.pruebaprueba.data.service.models.JobResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MindeeApi {

    companion object {
        const val ENQUEUE = "/v2/inferences/enqueue"
        const val JOB = "/v2/jobs/{jobId}"
        const val RESULT = "/v2/inferences/{inferenceId}"
    }

    @Multipart
    @POST(ENQUEUE)
    suspend fun uploadDocument(
        @Part("model_id") modelId: RequestBody,
        @Part file: MultipartBody.Part? = null,
        @Part("url") url: RequestBody? = null,
        @Part("file_base64") fileBase64: RequestBody? = null,
        @Part("raw_text") rawText: RequestBody? = null,
        @Part("polygon") polygon: RequestBody? = null,
        @Part("confidence") confidence: RequestBody? = null,
        @Part("rag") rag: RequestBody? = null,
        @Part("alias") alias: RequestBody? = null,
        @Part webhookId: List<MultipartBody.Part>? = null
    ): Response<JobResponseDTO>

    @GET(JOB)
    suspend fun getJob(@Path("jobId") jobId: String): Response<JobResponseDTO>

    @GET(RESULT)
    suspend fun getInference(@Path("inferenceId") jobId: String): Response<InferenceResponseDTO>
}