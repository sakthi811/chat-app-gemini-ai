package com.droiddude.apps.sample_gemini_ai_app.screens.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droiddude.apps.sample_gemini_ai_app.GenerativeViewModelFactory
import com.droiddude.apps.sample_gemini_ai_app.ui.theme.Sample_Gemini_AI_AppTheme
import com.google.ai.*

@Composable
inline fun SummarizeRoute(
    summaryViewModel: SummaryViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val summaryUiState by summaryViewModel.uiState.collectAsState()

    TextSummaryScreen(summaryUiState, onSummarizeClicked = { inputText ->
        summaryViewModel.summarize(inputText)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSummaryScreen(
    uiState: SummaryUiState = SummaryUiState.Loading,
    onSummarizeClicked : (String) -> Unit = {}
) {
    var textToSummarize by rememberSaveable {
        mutableStateOf("Android Jetpack Compose")
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            OutlinedTextField(
                value = textToSummarize,
                label = { Text("Text") },
                onValueChange = { textToSummarize = it },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            TextButton(
                onClick = {
                    if (textToSummarize.isNotBlank()) {
                        onSummarizeClicked(textToSummarize)
                    }
                    textToSummarize = ""
                },
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Text("Go")
            }
        }

        when (uiState) {
            SummaryUiState.Initial -> {

            }

            SummaryUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 16.dp)
                ) {
                    //CircularProgressIndicator()
                }
            }

            is SummaryUiState.Success -> {
                Column {
                    Card(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Person Icon",
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .requiredSize(36.dp)
                                    .drawBehind {
                                        drawCircle(color = Color.White)
                                    }
                            )
                            Text(
                                text = uiState.outputText, // TODO(thatfiredev): Figure out Markdown support
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }

            is SummaryUiState.Error -> {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(all = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SummaryScreenPreview() {
    Sample_Gemini_AI_AppTheme {
           TextSummaryScreen()
    }
}