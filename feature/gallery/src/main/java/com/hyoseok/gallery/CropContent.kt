import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun CropView(
    imageBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    cropStrokeColor: Color,
    cropStrokeWidth: Dp,
    initialCropSize: Size = Size(200f, 200f),
    contentScale: ContentScale = ContentScale.Fit,
    onCrop: (Bitmap) -> Unit
) {
    var cropRect by remember {
        mutableStateOf(Rect(Offset.Zero, initialCropSize))
    }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isResizing by remember { mutableStateOf(false) }
    var resizeEdge by remember { mutableStateOf<ResizeEdge?>(null) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 3f)
                    offset += pan
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        val touchX = it.x
                        val touchY = it.y

                        val resizeTolerance = 20.dp.toPx()

                        resizeEdge = when {
                            // 모서리
                            touchX in (cropRect.left - resizeTolerance)..(cropRect.left + resizeTolerance) &&
                                    touchY in (cropRect.top - resizeTolerance)..(cropRect.top + resizeTolerance) -> ResizeEdge.TOP_LEFT

                            touchX in (cropRect.right - resizeTolerance)..(cropRect.right + resizeTolerance) &&
                                    touchY in (cropRect.top - resizeTolerance)..(cropRect.top + resizeTolerance) -> ResizeEdge.TOP_RIGHT

                            touchX in (cropRect.left - resizeTolerance)..(cropRect.left + resizeTolerance) &&
                                    touchY in (cropRect.bottom - resizeTolerance)..(cropRect.bottom + resizeTolerance) -> ResizeEdge.BOTTOM_LEFT

                            touchX in (cropRect.right - resizeTolerance)..(cropRect.right + resizeTolerance) &&
                                    touchY in (cropRect.bottom - resizeTolerance)..(cropRect.bottom + resizeTolerance) -> ResizeEdge.BOTTOM_RIGHT
                            // 옆 면
                            touchX in (cropRect.left - resizeTolerance)..(cropRect.left + resizeTolerance) -> ResizeEdge.LEFT
                            touchX in (cropRect.right - resizeTolerance)..(cropRect.right + resizeTolerance) -> ResizeEdge.RIGHT
                            touchY in (cropRect.top - resizeTolerance)..(cropRect.top + resizeTolerance) -> ResizeEdge.TOP
                            touchY in (cropRect.bottom - resizeTolerance)..(cropRect.bottom + resizeTolerance) -> ResizeEdge.BOTTOM
                            else -> null
                        }

                        isResizing = resizeEdge != null
                    },
                    onDrag = { change, dragAmount ->
                        if (isResizing) {
                            when (resizeEdge) {
                                ResizeEdge.LEFT -> {
                                    val newLeft = (cropRect.left + dragAmount.x).coerceIn(
                                        0f,
                                        cropRect.right - 50f
                                    )
                                    cropRect = Rect(
                                        Offset(newLeft, cropRect.top),
                                        Size(cropRect.right - newLeft, cropRect.height)
                                    )
                                }

                                ResizeEdge.RIGHT -> {
                                    val newRight = (cropRect.right + dragAmount.x).coerceIn(
                                        cropRect.left + 50f,
                                        imageBitmap.width.toFloat()
                                    )
                                    cropRect = Rect(
                                        cropRect.topLeft,
                                        Size(newRight - cropRect.left, cropRect.height)
                                    )
                                }

                                ResizeEdge.TOP -> {
                                    val newTop = (cropRect.top + dragAmount.y).coerceIn(
                                        0f,
                                        cropRect.bottom - 50f
                                    )
                                    cropRect = Rect(
                                        Offset(cropRect.left, newTop),
                                        Size(cropRect.width, cropRect.bottom - newTop)
                                    )
                                }

                                ResizeEdge.BOTTOM -> {
                                    val newBottom = (cropRect.bottom + dragAmount.y).coerceIn(
                                        cropRect.top + 50f,
                                        imageBitmap.height.toFloat()
                                    )
                                    cropRect = Rect(
                                        cropRect.topLeft,
                                        Size(cropRect.width, newBottom - cropRect.top)
                                    )
                                }

                                ResizeEdge.TOP_LEFT -> {
                                    val newLeft = (cropRect.left + dragAmount.x).coerceIn(
                                        0f,
                                        cropRect.right - 50f
                                    )
                                    val newTop = (cropRect.top + dragAmount.y).coerceIn(
                                        0f,
                                        cropRect.bottom - 50f
                                    )
                                    cropRect = Rect(
                                        Offset(newLeft, newTop),
                                        Size(cropRect.right - newLeft, cropRect.bottom - newTop)
                                    )
                                }

                                ResizeEdge.TOP_RIGHT -> {
                                    val newRight = (cropRect.right + dragAmount.x).coerceIn(
                                        cropRect.left + 50f,
                                        imageBitmap.width.toFloat()
                                    )
                                    val newTop = (cropRect.top + dragAmount.y).coerceIn(
                                        0f,
                                        cropRect.bottom - 50f
                                    )
                                    cropRect = Rect(
                                        Offset(cropRect.left, newTop),
                                        Size(newRight - cropRect.left, cropRect.bottom - newTop)
                                    )
                                }

                                ResizeEdge.BOTTOM_LEFT -> {
                                    val newLeft = (cropRect.left + dragAmount.x).coerceIn(
                                        0f,
                                        cropRect.right - 50f
                                    )
                                    val newBottom = (cropRect.bottom + dragAmount.y).coerceIn(
                                        cropRect.top + 50f,
                                        imageBitmap.height.toFloat()
                                    )
                                    cropRect = Rect(
                                        Offset(newLeft, cropRect.top),
                                        Size(cropRect.right - newLeft, newBottom - cropRect.top)
                                    )
                                }

                                ResizeEdge.BOTTOM_RIGHT -> {
                                    val newRight = (cropRect.right + dragAmount.x).coerceIn(
                                        cropRect.left + 50f,
                                        imageBitmap.width.toFloat()
                                    )
                                    val newBottom = (cropRect.bottom + dragAmount.y).coerceIn(
                                        cropRect.top + 50f,
                                        imageBitmap.height.toFloat()
                                    )
                                    cropRect = Rect(
                                        cropRect.topLeft,
                                        Size(newRight - cropRect.left, newBottom - cropRect.top)
                                    )
                                }

                                else -> Unit
                            }
                        } else {
                            // 영역 조절이 아닌 영역 이동 시
                            cropRect = cropRect.translate(dragAmount)
                        }
                        change.consume()
                    },
                    onDragEnd = {
                        val croppedBitmap = Bitmap.createBitmap(
                            imageBitmap.asAndroidBitmap(),
                            cropRect.left
                                .toInt()
                                .coerceIn(0, imageBitmap.width - cropRect.width.toInt()),
                            cropRect.top
                                .toInt()
                                .coerceIn(0, imageBitmap.height - cropRect.height.toInt()),
                            cropRect.width
                                .toInt()
                                .coerceIn(1, imageBitmap.width),
                            cropRect.height
                                .toInt()
                                .coerceIn(1, imageBitmap.height)
                        )
                        onCrop(croppedBitmap)
                        isResizing = false
                        resizeEdge = null
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val containerWidth = constraints.maxWidth.toFloat()
            val containerHeight = constraints.maxHeight.toFloat()

            val scaledImageSize = when (contentScale) {
                ContentScale.Fit -> {
                    val scaleFactor = minOf(
                        containerWidth / imageBitmap.width,
                        containerHeight / imageBitmap.height
                    )
                    Size(imageBitmap.width * scaleFactor, imageBitmap.height * scaleFactor)
                }
                ContentScale.Crop -> {
                    val scaleFactor = maxOf(
                        containerWidth / imageBitmap.width,
                        containerHeight / imageBitmap.height
                    )
                    Size(imageBitmap.width * scaleFactor, imageBitmap.height * scaleFactor)
                }
                ContentScale.FillBounds -> Size(containerWidth, containerHeight)
                ContentScale.FillHeight -> {
                    val scaleFactor = containerHeight / imageBitmap.height
                    Size(imageBitmap.width * scaleFactor, containerHeight)
                }
                ContentScale.FillWidth -> {
                    val scaleFactor = containerWidth / imageBitmap.width
                    Size(containerWidth, imageBitmap.height * scaleFactor)
                }
                ContentScale.Inside -> {
                    if (imageBitmap.width <= containerWidth && imageBitmap.height <= containerHeight) {
                        Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat())
                    } else {
                        val scaleFactor = minOf(
                            containerWidth / imageBitmap.width,
                            containerHeight / imageBitmap.height
                        )
                        Size(imageBitmap.width * scaleFactor, imageBitmap.height * scaleFactor)
                    }
                }
                ContentScale.None -> Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat())
                else -> {
                    Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat())
                }
            }

            // 초기 cropRect를 중앙에 배치
            if (cropRect == Rect(Offset.Zero, initialCropSize)) {
                cropRect = Rect(
                    Offset(
                        (containerWidth - initialCropSize.width) / 2,
                        (containerHeight - initialCropSize.height) / 2
                    ),
                    initialCropSize
                )
            }

            withTransform({
                translate(offset.x, offset.y)
                scale(scale, pivot = Offset.Zero)
            }) {
                val scaledWidth = scaledImageSize.width.toInt()
                val scaledHeight = scaledImageSize.height.toInt()

                // 이미지가 그려지는 좌상단 좌표
                val imageStartX = ((containerWidth - scaledWidth) / 2).toInt()
                val imageStartY = ((containerHeight - scaledHeight) / 2).toInt()

                // 이미지를 그립니다
                drawImage(
                    image = imageBitmap,
                    dstOffset = IntOffset(imageStartX, imageStartY),
                    dstSize = IntSize(scaledWidth, scaledHeight)
                )

                // 크롭 영역이 이미지 내에 제한되도록 cropRect를 조정합니다.
                val adjustedCropRect = Rect(
                    topLeft = Offset(
                        cropRect.left.coerceIn(imageStartX.toFloat(), (imageStartX + scaledWidth).toFloat()),
                        cropRect.top.coerceIn(imageStartY.toFloat(), (imageStartY + scaledHeight).toFloat())
                    ),
                    bottomRight = Offset(
                        cropRect.right.coerceIn(imageStartX.toFloat(), (imageStartX + scaledWidth).toFloat()),
                        cropRect.bottom.coerceIn(imageStartY.toFloat(), (imageStartY + scaledHeight).toFloat())
                    )
                )

                // 크롭 영역과 마스크를 그립니다
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    drawRect(Color(0x75000000))
                    drawRoundRect(
                        topLeft = adjustedCropRect.topLeft,
                        size = adjustedCropRect.size,
                        cornerRadius = CornerRadius(10f, 10f),
                        color = Color.Transparent,
                        blendMode = androidx.compose.ui.graphics.BlendMode.Clear
                    )
                    drawRoundRect(
                        topLeft = adjustedCropRect.topLeft,
                        size = adjustedCropRect.size,
                        cornerRadius = CornerRadius(10f, 10f),
                        color = cropStrokeColor,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = cropStrokeWidth.toPx())
                    )
                    restoreToCount(checkPoint)
                }
            }
        }
    }
}



enum class ResizeEdge {
    LEFT, RIGHT, TOP, BOTTOM, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
}
