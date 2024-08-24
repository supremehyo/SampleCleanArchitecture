package com.hyoseok.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hyoseok.samplecleanarchitecture.core.designsystem.component.TextButtonComponent
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.LocalTypography
import com.hyoseok.samplecleanarchitecture.core.model.UiState
import feature.gallery.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun GalleryImageScreen(
    galleryViewModel: GalleryViewModel = hiltViewModel(),
    onCancel: () -> Unit,
    ioDispatcher: CoroutineDispatcher,
    coroutineScope: CoroutineScope
) {
    var selectKey = ""
    var croppedBitmapList by remember { mutableStateOf(mutableStateMapOf<String,ImageBitmap>()) }
    var isCropRequested by remember { mutableStateOf(false) }
    var loadComplete by remember { mutableStateOf(false) }
    var cropActive by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            selectedImageUris = uris
            if (selectedImageUris.isNotEmpty()) {
                selectedImageUri = selectedImageUris.first()
            } else {
                onCancel.invoke()
            }
        }
    )

    LaunchedEffect(true) {
        multiplePhotoPickerLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    LaunchedEffect(selectedImageUris) {
        coroutineScope.launch(ioDispatcher + coroutineExceptionHandler) {
            galleryViewModel.updateGalleryScreenState(selectedImageUris)
        }
    }

    LaunchedEffect(true) {
        coroutineScope.launch(ioDispatcher + coroutineExceptionHandler) {
            galleryViewModel.galleryEventFlow.collect { state ->
                loadComplete = when (state) {
                    is GalleryViewModel.GalleryEvent.GalleryUiEvent -> {
                        when (state.uiState) {
                            is UiState.Loading -> false
                            is UiState.Success -> true
                            is UiState.Error -> false
                        }
                    }
                }
            }
        }
    }

    if (loadComplete) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "사진 편집", style = LocalTypography.current.headlineMediumB)
            TextButtonComponent(
                name = "자르기",
                backColor = Color.Black,
                textColor = Color.White,
                modifier = Modifier.align(Alignment.End)
            ) {
                cropActive = true
            }
            if (cropActive) {
                selectedImageUri?.let {
                    uriToImageBitmap(LocalContext.current, it)?.let {
                        Box(modifier = Modifier.size(400.dp)) {
                            com.choidev.cropview.CropView(
                                imageBitmap = it,
                                cropStrokeColor = Color.Black,
                                cropStrokeWidth = 4.dp,
                                onCrop = { bitmap , complete, key,->
                                    croppedBitmapList.put(key,bitmap)
                                    isCropRequested = complete
                                    cropActive = false
                                },
                                onRequestCrop = isCropRequested,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(30.dp)
                        .clickable {
                            isCropRequested = true
                        },
                    contentAlignment = Alignment.Center,
                ){
                    Text(text = "Crop", style = TextStyle(color = Color.White))
                }
            } else {
                if (croppedBitmapList.isNotEmpty()){
                    Image(
                        modifier = Modifier.size(400.dp),
                        bitmap = croppedBitmapList.get(selectKey)!!,
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }else{
                    selectedImageUri?.let {
                        uriToImageBitmap(LocalContext.current, it)?.let {
                            Image(
                                modifier = Modifier.size(400.dp),
                                bitmap = it,
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                Column {
                    Text(
                        text = "선택하여 편집 이미지를 변경 할 수 있습니다.",
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                    )
                    LazyColumn(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        itemsIndexed(selectedImageUris) {index, uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        selectedImageUri = uri
                                        selectKey = index.toString()
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

        }
    }
}


fun uriToImageBitmap(context: Context, uri: Uri): ImageBitmap? {
    return try {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            } ?: throw IllegalArgumentException("Unable to open InputStream from URI")
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}