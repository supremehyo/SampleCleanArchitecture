package com.hyoseok.samplecleanarchitecture.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.hyoseok.samplecleanarchitecture.core.designsystem.component.TextButtonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun PictureScreen(
    savedUri: String,
    retry : ()->Unit,
    complete : ()-> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(savedUri) {
        val contentUri = getImageContentUri(context, File(savedUri))
        bitmap = contentUri?.let { loadBitmapFromUri(context, it) }
    }

    bitmap?.let {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButtonComponent(
                    name = "재촬영",
                    backColor = Color.White,
                    textColor = Color.Black
                ) {
                    //재촬영시 지금 사진이 필요없어서 삭제하고 retry 를 호출
                    deleteCurrentPicture(
                        context = context,
                        savedUri = savedUri,
                        onDeleteComplete = {
                            retry.invoke()
                        }
                    )
                }
                TextButtonComponent(
                    name = "확인",
                    backColor = Color.White,
                    textColor = Color.Black
                ) {
                    complete.invoke()
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "이미지를 불러오지 못했습니다.")
        }
    }
}

fun deleteCurrentPicture(context: Context, savedUri: String, onDeleteComplete: () -> Unit) {
    val file = File(savedUri)
    if (file.exists()) {
        // 삭제 전, 파일의 Content Uri를 가져옵니다.
        val contentUri = getImageContentUri(context, file)

        // 파일을 시스템에서 삭제합니다.
        val fileDeleted = file.delete()

        // 미디어 스토어에서도 삭제합니다.
        contentUri?.let { uri ->
            val rowsDeleted = context.contentResolver.delete(uri, null, null)
            if (rowsDeleted > 0 || fileDeleted) {
                onDeleteComplete()
            } else {
                //TODO 토스트 메세지로 변경 예정
                // 삭제에 실패한 경우, 로그 또는 에러 처리
                println("파일 삭제에 실패했습니다.")
            }
        } ?: run {
            if (fileDeleted) {
                onDeleteComplete()
            } else {
                // 삭제에 실패한 경우, 로그 또는 에러 처리
                println("파일 삭제에 실패했습니다.")
            }
        }
    } else {
        // 파일이 존재하지 않는 경우
        println("파일이 존재하지 않습니다.")
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

}