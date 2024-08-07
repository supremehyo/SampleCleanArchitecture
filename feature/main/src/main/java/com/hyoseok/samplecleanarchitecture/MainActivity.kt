package com.hyoseok.samplecleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.SampleCleanArchitectureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleCleanArchitectureTheme {
                MainScreen()
            }
        }
    }
}