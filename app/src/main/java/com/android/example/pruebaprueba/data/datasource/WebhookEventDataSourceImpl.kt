package com.android.example.pruebaprueba.data.datasource

import com.android.example.pruebaprueba.core.exception.Failure
import com.android.example.pruebaprueba.domain.models.InferenceState
import com.android.example.pruebaprueba.models.mindee.InferenceResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class WebhookEventDataSourceImpl @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : WebhookEventDataSource {
    override fun observeJob(jobId: String): Flow<InferenceState> {
        val flow = MutableSharedFlow<InferenceState>(replay = 1)
        val request = Request.Builder().url("https://api-v2.mindee.net/inferences/$jobId").build()

        client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val payload = try {
                    gson.fromJson(text, InferenceResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                if (payload != null) {
                    flow.tryEmit(InferenceState.Completed(payload))
                } else {
                    flow.tryEmit(InferenceState.Error(Failure.Throwable(Throwable("Bad webhook payload"))))
                }
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                super.onFailure(webSocket, t, response)

                flow.tryEmit(InferenceState.Error(Failure.Throwable(t)))
            }
        })
        return flow
    }

}