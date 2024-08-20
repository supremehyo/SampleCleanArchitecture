package com.hyoseok.compose_camera

import androidx.compose.runtime.Immutable

@Immutable
data class RecordingInfo(
    val duration: Long,
    val sizeByte: Long,
    val audioAmplitude : Double
)