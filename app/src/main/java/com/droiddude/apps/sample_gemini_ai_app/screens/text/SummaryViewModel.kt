package com.droiddude.apps.sample_gemini_ai_app.screens.text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class SummaryViewModel(
    private val generativeModel : GenerativeModel
) : ViewModel() {
    private val _uiState : MutableStateFlow<SummaryUiState> =
        MutableStateFlow(SummaryUiState.Initial)

    val uiState = _uiState.asStateFlow()

    fun summarize(inputText : String) {
        _uiState.value = SummaryUiState.Loading

        val prompt = "Summarize the following text for me: $inputText"
        viewModelScope.launch {
            try {
                var output = ""
                generativeModel.generateContentStream(prompt)
                    .collect { response ->
                        output += response.text
                        _uiState.value = SummaryUiState.Success(output)
                    }
            } catch (e: Exception) {
                _uiState.value = SummaryUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}