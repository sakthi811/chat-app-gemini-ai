package com.droiddude.apps.sample_gemini_ai_app.screens.text

sealed interface SummaryUiState {

    data object Initial : SummaryUiState
    data object Loading : SummaryUiState
    data class Success(
        val outputText : String
    ) : SummaryUiState
    data class Error(
        val errorMessage: String
    ) : SummaryUiState
}