package com.hyoseok.samplecleanarchitecture.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.LocalTypography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun PictureScreen(savedUri: String) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(savedUri) {
        val contentUri = getImageContentUri(context, File(savedUri))
        bitmap = contentUri?.let { loadBitmapFromUri(context, it) }
    }

    bitmap?.let {
        Column(
            modifier = Modifier.background(Color.Black),
            verticalArrangement = Arrangement.Center
        ) {
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "재촬영" , style = LocalTypography.current.displaySmallR)
                Text(text = "확인" , style = LocalTypography.current.displaySmallR)
            }
        }
    } ?: run {
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = "이미지를 불러오지 못했습니다.")
        }
    }
}

suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun getImageContentUri(context: Context, imageFile: File): Uri? {
    val filePath = imageFile.absolutePath
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=? ",
        arrayOf(filePath), null
    )
    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
        val baseUri = Uri.parse("content://media/external/images/media")
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (imageFile.exists()) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DATA, filePath)
            }
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PictureScreen("dd")
}