package com.droiddude.apps.sample_gemini_ai_app.screens.photo

import com.droiddude.apps.sample_gemini_ai_app.screens.text.SummaryUiState

sealed interface PhotoSummaryUiState {

    data object Initial : PhotoSummaryUiState
    data object Loading : PhotoSummaryUiState
    data class Success(
        val outputText : String
    ) : PhotoSummaryUiState
    data class Error(
        val errorMessage: String
    ) : PhotoSummaryUiState

}