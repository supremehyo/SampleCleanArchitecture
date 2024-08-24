package com.hyoseok.gallery

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale

data class TextFieldData(
    var offset: Offset,
    var textState: TextFieldValue,
    val focusRequester: FocusRequester = FocusRequester(),
    var isFocused: Boolean = false
)

@Composable
fun ImageOnTextScreen(
    modifier: Modifier,
    imageBitmap: ImageBitmap,
    onText: (ImageBitmap) -> Unit
) {
    var textFields by remember { mutableStateOf(listOf<TextFieldData>()) }
    val focusManager: FocusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val anyFocused = textFields.any { it.isFocused }
                        if (anyFocused) {
                            focusManager.clearFocus()
                        } else {
                            textFields = textFields + TextFieldData(
                                offset = tapOffset,
                                textState = TextFieldValue("")
                            )
                        }
                    }
                )
            }
    ) {
        Image(imageBitmap, "", contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())


        textFields.forEachIndexed { index, textFieldData ->
            BasicTextField(
                value = textFieldData.textState,
                onValueChange = { newText ->
                    textFields = textFields.toMutableList().also {
                        it[index] = it[index].copy(textState = newText)
                    }
                },
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .absoluteOffset {
                        IntOffset(
                            textFieldData.offset.x.toInt(),
                            textFieldData.offset.y.toInt()
                        )
                    }
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = { },
                            onDragCancel = { },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                textFields = textFields
                                    .toMutableList()
                                    .also {
                                        it[index] = it[index].copy(
                                            offset = Offset(
                                                x = it[index].offset.x + dragAmount.x,
                                                y = it[index].offset.y + dragAmount.y
                                            )
                                        )
                                    }
                            }
                        )
                    }
                    .focusRequester(textFieldData.focusRequester)
                    .onFocusChanged { focusState ->
                        textFields = textFields
                            .toMutableList()
                            .also {
                                it[index] = it[index].copy(isFocused = focusState.isFocused)
                            }
                    }
                    .padding(8.dp)
            )
        }
    }
}
