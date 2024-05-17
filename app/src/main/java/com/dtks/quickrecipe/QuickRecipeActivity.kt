package com.dtks.quickrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dtks.quickrecipe.ui.navigation.QuickRecipeNavGraph
import com.dtks.quickrecipe.ui.theme.QuickRecipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickRecipeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickRecipeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuickRecipeNavGraph()
                }
            }
        }
    }
}