package com.droiddude.apps.sample_gemini_ai_app.screens.photo

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.droiddude.apps.sample_gemini_ai_app.GenerativeViewModelFactory
import com.droiddude.apps.sample_gemini_ai_app.util.UriSaver
import com.droiddude.apps.sample_gemini_ai_app.ui.theme.Sample_Gemini_AI_AppTheme
import kotlinx.coroutines.launch

@Composable
internal fun PhotoSummaryRoute(
    viewModel: PhotoSummaryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = GenerativeViewModelFactory)
) {
    val photoSummaryUiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    PhotoSummaryScreen(
        uiState = photoSummaryUiState,
        onSummaryClicked = { inputText, selectedItems ->
            coroutineScope.launch {
               val bitmaps = selectedItems.mapNotNull {
                   val imageRequest = imageRequestBuilder.data(it)
                       .size(size = 768)
                       .precision(Precision.EXACT)
                       .build()
                   try {
                       val result = imageLoader.execute(imageRequest)
                       if(result is SuccessResult) {
                           return@mapNotNull(result.drawable as BitmapDrawable).bitmap
                       } else {
                           return@mapNotNull null
                       }
                   }catch(e: Exception){
                       return@mapNotNull null
                   }
               }
                viewModel.summarize(inputText, bitmaps)
            }
        }
    )
}

@Composable
fun PhotoSummaryScreen(
    uiState: PhotoSummaryUiState = PhotoSummaryUiState.Loading,
    onSummaryClicked : (String, List<Uri>) -> Unit = { _, _ ->}
) {
    var userQuestion by rememberSaveable {
        mutableStateOf("")
    }
    val imageUris = rememberSaveable(saver = UriSaver()) {
        mutableStateListOf()
    }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            imageUris.add(it)
        }
    }
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
           modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add"
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = userQuestion,
                    onValueChange = { userQuestion = it },
                    label = { Text("Question")},
                    placeholder = {Text("Upload an image and then ask a question")})
                TextButton(
                    onClick = {
                        if (userQuestion.isNotBlank()) {
                            onSummaryClicked(userQuestion, imageUris.toList())
                        }
                    },
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("Go")
                }
            }
            LazyRow(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                items(imageUris) {imageUri ->
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .requiredSize(72.dp)
                    )
                }
            }
        }
        when(uiState) {
            PhotoSummaryUiState.Initial -> {

            }

            is PhotoSummaryUiState.Error -> {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
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
            PhotoSummaryUiState.Loading -> {

            }
            is PhotoSummaryUiState.Success -> {
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
    }
}

@Composable
@Preview(showSystemUi = true)
fun PhotoSummaryScreenPreview() {
    Sample_Gemini_AI_AppTheme {
        PhotoSummaryScreen()
    }
}