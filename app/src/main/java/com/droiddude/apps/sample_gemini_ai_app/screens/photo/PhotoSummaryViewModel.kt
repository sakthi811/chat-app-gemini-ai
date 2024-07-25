package com.droiddude.apps.sample_gemini_ai_app.screens.photo

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class PhotoSummaryViewModel(
    private val generativeModel : GenerativeModel
) : ViewModel() {
    private val _uiState : MutableStateFlow<PhotoSummaryUiState> =
        MutableStateFlow(PhotoSummaryUiState.Initial)

    val uiState : StateFlow<PhotoSummaryUiState> = _uiState.asStateFlow()

    fun summarize(inputText: String, selectedImages : List<Bitmap>) {
        _uiState.value = PhotoSummaryUiState.Loading
        val prompt = "Look at the image(s), and then answer the following question: $inputText"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for(bitmap in selectedImages) {
                        image(bitmap)
                    }
                    text(prompt)
                }
                var outputContent = ""
                generativeModel.generateContentStream(inputContent)
                    .collect{ response ->
                        outputContent += response.text
                        _uiState.value = PhotoSummaryUiState.Success(outputContent)
                    }
            } catch(e: Exception) {
                _uiState.value = PhotoSummaryUiState.Error(e.localizedMessage)
            }
        }
    }
}