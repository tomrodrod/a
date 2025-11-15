package com.android.example.pruebaprueba.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.pruebaprueba.domain.MindeeRepository
import com.android.example.pruebaprueba.domain.models.InferenceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: MindeeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: MutableStateFlow<UiState> = _state

    fun enqueueAndObserve(
        image: File,
        modelId: String,
        webhooksIds: List<String> = emptyList(),
        alias: String? = null
    ) {
        viewModelScope.launch {
            repository.enqueueImageAndObserve(
                file = image,
                modelId = modelId,
                webhookIds = webhooksIds,
                alias = alias
            ).collectLatest { state ->
                when (state) {
                    is InferenceState.Completed<*> -> _state.value =
                        UiState(result = state.data, isLoading = false)

                    is InferenceState.Enqueued -> _state.value =
                        UiState(enqueued = true, isLoading = true)

                    is InferenceState.Error -> _state.value =
                        UiState(error = state.failure.toString(), isLoading = false)

                    InferenceState.Processing -> _state.value =
                        UiState(isProcessing = true, isLoading = true)

                    InferenceState.Uploading -> _state.value =
                        UiState(isUploading = true, isLoading = true)
                }
            }

        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isProcessing: Boolean = false,
    val isUploading: Boolean = false,
    val enqueued: Boolean = false,
    val result: Any? = null
)