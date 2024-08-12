package com.hyoseok.samplecleanarchitecture.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuComponent(
    menuTitle : String,
    backgroundColor : Color,
    content: @Composable () -> Unit,
    onClick :()-> Unit
){
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoundImageComponent(
            backgroundColor = backgroundColor,
            content = content,
            onClick = {
                onClick.invoke()
            }
        )
        Text(text = menuTitle , textAlign = TextAlign.Center)
    }
}

@Composable
fun RoundImageComponent(
    backgroundColor : Color,
    onClick :()-> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(4.dp))
        .background(backgroundColor)
        .size(80.dp)
        .clickable {
            onClick.invoke()
        }
    ){
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    MenuComponent(
        menuTitle = "메뉴",
        backgroundColor = Color.Red,
        content = {

        },
        onClick = {

        }
    )
}