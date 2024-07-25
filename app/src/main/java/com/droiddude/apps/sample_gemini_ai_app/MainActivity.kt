package com.droiddude.apps.sample_gemini_ai_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.droiddude.apps.sample_gemini_ai_app.screens.chat.ChatRoute
import com.droiddude.apps.sample_gemini_ai_app.screens.photo.PhotoSummaryRoute
import com.droiddude.apps.sample_gemini_ai_app.screens.photo.PhotoSummaryScreen
import com.droiddude.apps.sample_gemini_ai_app.screens.text.SummarizeRoute
import com.droiddude.apps.sample_gemini_ai_app.ui.theme.Sample_Gemini_AI_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sample_Gemini_AI_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen( onItemClicked = { routeId ->
                                navController.navigate(routeId)
                            })
                        }
                        composable("text_summary") {
                            SummarizeRoute()
                        }
                        composable("photo") {
                            PhotoSummaryRoute()
                        }
                        composable("chat") {
                            ChatRoute()
                        }
                    }
                }
            }
        }
    }
}
