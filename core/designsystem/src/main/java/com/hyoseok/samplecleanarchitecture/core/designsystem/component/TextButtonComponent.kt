package com.hyoseok.samplecleanarchitecture.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.LocalTypography

@Composable
fun TextButtonComponent(
    name : String,
    backColor : Int,
    onClick : () -> Unit
) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ){
        Text(text = "재촬영" , style = LocalTypography.current.displaySmallR)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {

}