package com.hyoseok.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyoseok.samplecleanarchitecture.core.designsystem.component.MenuComponent
import feature.home.R

@Composable
fun HomeScreen(
    moveScreen : (String)-> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            MenuComponent(
                menuTitle = "촬영",
                backgroundColor = Color.LightGray,
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){
                        Image(
                            painter = painterResource(R.drawable.baseline_camera_alt_24),
                            contentDescription = "camera",
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                },
                onClick = {
                    moveScreen.invoke("CAMERA")
                }
            )

            MenuComponent(
                menuTitle = "편집",
                backgroundColor = Color.LightGray,
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){
                        Image(
                            painter = painterResource(R.drawable.baseline_camera_alt_24),
                            contentDescription = "edit",
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                },
                onClick = {
                    moveScreen.invoke("GALLERY")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen{

    }
}