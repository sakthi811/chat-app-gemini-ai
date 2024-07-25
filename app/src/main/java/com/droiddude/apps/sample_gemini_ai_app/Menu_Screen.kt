package com.droiddude.apps.sample_gemini_ai_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MenuItem(
    val routeId : String,
    val titleResId: String,
    val descriptionResId: String
)

@Composable
fun MenuScreen(
    onItemClicked :(String) -> Unit = {}
) {
    val menuItems = listOf(
        MenuItem("text_summary", "Generate Text from Text", ""),
        MenuItem("photo", "Generate Text from Text and Image", ""),
        MenuItem("chat", "Conversational chat", "")
    )

    LazyColumn(
        Modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        items(menuItems) { menuItem ->
           Card(
               modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
           ) {
               Column(
                   modifier = Modifier.
                   padding(all = 16.dp)
                       .fillMaxWidth()
               ) {
                   Text(
                       text = menuItem.titleResId,
                       style = MaterialTheme.typography.titleMedium
                   )
                   TextButton(
                       onClick = {
                           onItemClicked(menuItem.routeId)
                       },
                       modifier = Modifier.align(Alignment.End)
                   ) {
                       Text(text = "Try it")
                   }
               }
           }
        }
    }
}