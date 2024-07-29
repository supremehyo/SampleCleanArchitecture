package com.hyoseok.compose_camera

sealed class RecordingState {
    object Idle : RecordingState()
    object OnRecord : RecordingState()
    object Paused : RecordingState()

}