package com.hyoseok.samplecleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.SampleCleanArchitectureTheme
import com.hyoseok.samplecleanarchitecture.di.ApplicationScope
import com.hyoseok.samplecleanarchitecture.di.IoDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleCleanArchitectureTheme {
                MainScreen(ioDispatcher , applicationScope)
            }
        }
    }
}