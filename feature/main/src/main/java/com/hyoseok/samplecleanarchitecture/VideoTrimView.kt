package com.hyoseok.samplecleanarchitecture

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//TODO 다른 모듈로 이동 필요
@Composable
fun VideoTrimView(
    modifier: Modifier = Modifier,
    onRangeChange: (Float, Float) -> Unit // 범위 변경 콜백
) {
    var startHandlePosition by remember { mutableStateOf(0f) }
    var endHandlePosition by remember { mutableStateOf(1f) }

    Box(modifier = modifier.height(80.dp)) { // View의 기본 크기 설정
        var boxWidth by remember { mutableStateOf(0f) }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                boxWidth = layoutCoordinates.size.width.toFloat()
            }
        ) {
            // 배경 바 그리기
            drawRoundRect(
                color = Color.LightGray,
                size = size.copy(width = size.width),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )
            // 선택된 범위 표시
            drawRoundRect(
                color = Color.Yellow,
                size = size.copy(width = size.width * (endHandlePosition - startHandlePosition)),
                topLeft = Offset(x = size.width * startHandlePosition, y = 0f),
                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )
        }

        val startOffsetDp = with(LocalDensity.current) { (startHandlePosition * boxWidth).toDp() }
        val endOffsetDp = with(LocalDensity.current) { (endHandlePosition * boxWidth).toDp() }

        // 왼쪽 핸들
        Box(
            modifier = Modifier
                .offset(x = startOffsetDp)
                .size(24.dp)
                .background(Color.Red)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        val newPosition = (startHandlePosition + dragAmount.x / boxWidth).coerceIn(0f, endHandlePosition)
                        startHandlePosition = newPosition
                        onRangeChange(startHandlePosition, endHandlePosition)
                    }
                }
        )

        // 오른쪽 핸들
        Box(
            modifier = Modifier
                .offset(x = endOffsetDp - 24.dp)
                .size(24.dp)
                .background(Color.Red)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        val newPosition = (endHandlePosition + dragAmount.x / boxWidth).coerceIn(startHandlePosition, 1f)
                        endHandlePosition = newPosition
                        onRangeChange(startHandlePosition, endHandlePosition)
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVideoTrimView() {
    VideoTrimView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onRangeChange = { start, end ->
            // 범위 변경 시 동작할 로직
            println("Start: $start, End: $end")
        }
    )
}
