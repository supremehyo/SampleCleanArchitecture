package com.hyoseok.common.util

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

@Composable
fun captureComposableAsBitmap(): Bitmap {
    val context = LocalContext.current
    val density = LocalDensity.current
    val view = LocalView.current

    // Composable의 크기를 측정하기 위해 View의 크기와 Density를 사용
    val width = with(density) { 300.dp.roundToPx() }
    val height = with(density) { 200.dp.roundToPx() }

    // Bitmap 생성
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    // Canvas에 Composable 그리기
    val canvas = android.graphics.Canvas(bitmap)
    view.draw(canvas)

    return bitmap
}