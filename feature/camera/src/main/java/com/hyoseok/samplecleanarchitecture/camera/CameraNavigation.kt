package com.hyoseok.samplecleanarchitecture.camera

import androidx.compose.runtime.Composable


sealed class  CameraPath(val path : String){
    object Picture : CameraPath("PICTURE")
    object Video : CameraPath("VIDEO")
}

@Composable
fun CameraNavigation(

) {
}