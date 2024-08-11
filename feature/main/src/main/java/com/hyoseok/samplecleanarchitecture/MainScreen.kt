package com.hyoseok.samplecleanarchitecture

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.hyoseok.samplecleanarchitecture.camera.CameraScreen

@Composable
fun MainScreen(){
    Column {
        VideoTrimView(
            modifier = Modifier.fillMaxWidth(),
            onRangeChange = { start, end ->

            }
        )
    }
}