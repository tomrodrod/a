package com.android.example.pruebaprueba.data

import com.android.example.pruebaprueba.core.exception.Failure
import com.android.example.pruebaprueba.core.extensions.onFailure
import com.android.example.pruebaprueba.core.extensions.onSuccess
import com.android.example.pruebaprueba.data.datasource.MindeeDataSourceService
import com.android.example.pruebaprueba.data.datasource.WebhookEventDataSource
import com.android.example.pruebaprueba.domain.MindeeRepository
import com.android.example.pruebaprueba.domain.models.InferenceState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MindeeRepositoryImpl @Inject constructor(
    private val dataSourceService: MindeeDataSourceService,
    private val webhookEventDataSource: WebhookEventDataSource
) : MindeeRepository {

    override fun enqueueImageAndObserve(
        file: File,
        modelId: String,
        webhookIds: List<String>,
        alias: String?
    ): Flow<InferenceState> = channelFlow {

        trySend(InferenceState.Uploading)

        val imagePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = file.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val modelIdBody = modelId.toRequestBody("text/plain".toMediaTypeOrNull())
        val aliasBody = alias?.toRequestBody("text/plain".toMediaTypeOrNull())
        val rawText = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val confidence = "true".toRequestBody("text/plain".toMediaTypeOrNull())

        val webhookParts = if (webhookIds.isNotEmpty()) {
            webhookIds.map { id ->
                MultipartBody.Part.createFormData(
                    name = "webhook_ids",
                    filename = null,
                    body = id.toRequestBody("text/plain".toMediaTypeOrNull())
                )
            }
        } else {
            null
        }

        dataSourceService.enqueue(
            modelId = modelIdBody,
            file = imagePart,
            webhookIds = webhookParts,
            rawText = rawText,
            confidence = confidence,
            alias = aliasBody
        )
            .onFailure { failure ->
                trySend(InferenceState.Error(failure))
            }
            .onSuccess { success ->
                val jobId = success.job?.id.orEmpty()
                trySend(InferenceState.Enqueued(jobId))

                if (webhookIds.isNotEmpty()) {

                    webhookEventDataSource.observeJob(jobId)
                        .collectLatest { result ->
                            trySend(result)
                            if (result is InferenceState.Completed<*> || result is InferenceState.Error) {
                                close()
                            }
                        }
                } else {
                    pollUntilDone(jobId)
                }
            }
    }

    private suspend fun ProducerScope<InferenceState>.pollUntilDone(jobId: String) {
        trySend(InferenceState.Processing)

        while (true) {

            dataSourceService.getJob(jobId)
                .onFailure { failure ->
                    trySend(InferenceState.Error(failure))
                }
                .onSuccess { success ->
                    val status = success.job?.status.orEmpty()
                    when (status) {
                        "Processing" -> {
                            delay(1500)
                        }

                        "Failed" -> {
                            trySend(
                                InferenceState.Error(
                                    Failure.Throwable(Throwable("Mindee job failed"))
                                )
                            )
                            return
                        }

                        "Processed" -> {
                            val resultUrl = success.job?.resultUrl.orEmpty()
                            val inferenceId = resultUrl.substringAfterLast("/")
                            if (!inferenceId.isNullOrEmpty()) {
                                dataSourceService.getInference(inferenceId)
                                    .onFailure { failure ->
                                        trySend(InferenceState.Error(failure))
                                    }
                                    .onSuccess { success ->
                                        trySend(InferenceState.Completed(success.inference))
                                    }
                            } else {
                                trySend(InferenceState.Completed(success.job))
                            }
                        }

                        else -> {
                            delay(1500)
                        }
                    }
                }

        }
    }
}