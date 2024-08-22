package com.hyoseok.samplecleanarchitecture.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.LocalTypography

@Composable
fun TextButtonComponent(
    name: String,
    backColor: Color,
    textColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backColor)
            .clickable {
                onClick.invoke()
            }
    ) {
        Text(
            text = name,
            style = LocalTypography.current.titleLargeB.copy(textColor),
            modifier = modifier.padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {

}