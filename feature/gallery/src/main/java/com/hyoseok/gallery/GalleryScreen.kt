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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
    var croppedBitmapList by remember { mutableStateOf(mutableStateMapOf<String, ImageBitmap>()) }
    var isCropRequested by remember { mutableStateOf(false) }
    var loadComplete by remember { mutableStateOf(false) }
    var cropActive by remember { mutableStateOf(false) }
    var onTextActive by remember { mutableStateOf(false) }
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
        GalleryScreenContent(
            selectedImageUri = selectedImageUri,
            selectedImageUris = selectedImageUris,
            cropActive = cropActive,
            onTextActive = onTextActive,
            croppedBitmapList = croppedBitmapList,
            selectKey = selectKey,
            isCropRequested = isCropRequested,
            onCropActiveChange = { cropActive = it },
            onTextActiveChange = { onTextActive = it },
            onCropRequestChange = { isCropRequested = it },
            onImageSelect = { uri, key ->
                selectedImageUri = uri
                selectKey = key
            },
            onCropComplete = { bitmap, key ->
                croppedBitmapList[key] = bitmap
                cropActive = false
            }
        )
    }
}

@Composable
fun GalleryScreenContent(
    selectedImageUri: Uri?,
    selectedImageUris: List<Uri>,
    cropActive: Boolean,
    onTextActive: Boolean,
    croppedBitmapList: Map<String, ImageBitmap>,
    selectKey: String,
    isCropRequested: Boolean,
    onCropActiveChange: (Boolean) -> Unit,
    onTextActiveChange: (Boolean) -> Unit,
    onCropRequestChange: (Boolean) -> Unit,
    onImageSelect: (Uri, String) -> Unit,
    onCropComplete: (ImageBitmap, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "사진 편집", style = LocalTypography.current.headlineMediumB)

        ActionButtons(
            cropActive = cropActive,
            onTextActive = onTextActive,
            onCropActiveChange = onCropActiveChange,
            onTextActiveChange = onTextActiveChange
        )

        when {
            onTextActive -> TextEditingView(selectedImageUri)
            cropActive -> CropViewContent(
                selectedImageUri = selectedImageUri,
                isCropRequested = isCropRequested,
                onCropRequestChange = onCropRequestChange,
                onCropComplete = onCropComplete
            )
            else -> ImagePreview(
                selectedImageUri = selectedImageUri,
                selectedImageUris = selectedImageUris,
                croppedBitmapList = croppedBitmapList,
                selectKey = selectKey,
                onImageSelect = onImageSelect
            )
        }
    }
}

@Composable
fun ActionButtons(
    cropActive: Boolean,
    onTextActive: Boolean,
    onCropActiveChange: (Boolean) -> Unit,
    onTextActiveChange: (Boolean) -> Unit
) {
    Row() {
        TextButtonComponent(
            name = "자르기",
            backColor = Color.Black,
            textColor = Color.White,
            modifier = Modifier
        ) {
            if (!onTextActive) onCropActiveChange(true)
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButtonComponent(
            name = "텍스트 입력",
            backColor = Color.Black,
            textColor = Color.White,
            modifier = Modifier
        ) {
            if (!cropActive) onTextActiveChange(true)
        }
    }
}

@Composable
fun TextEditingView(selectedImageUri: Uri?) {
    selectedImageUri?.let { uri ->
        uriToImageBitmap(LocalContext.current, uri)?.let { imageBitmap ->
            Box(modifier = Modifier.size(400.dp)) {
                ImageOnTextScreen(
                    modifier = Modifier,
                    imageBitmap = imageBitmap,
                    onText = { /* 텍스트 입력 로직 */ }
                )
            }
        }
    }
    CompleteButton { /* 완료 버튼 클릭 로직 */ }
}

@Composable
fun CropViewContent(
    selectedImageUri: Uri?,
    isCropRequested: Boolean,
    onCropRequestChange: (Boolean) -> Unit,
    onCropComplete: (ImageBitmap, String) -> Unit
) {
    selectedImageUri?.let { uri ->
        uriToImageBitmap(LocalContext.current, uri)?.let { imageBitmap ->
            Box(modifier = Modifier.size(400.dp)) {
                com.choidev.cropview.CropView(
                    imageBitmap = imageBitmap,
                    cropStrokeColor = Color.Black,
                    cropStrokeWidth = 4.dp,
                    onCrop = { bitmap, complete, key ->
                        onCropComplete(bitmap, key)
                        onCropRequestChange(false)
                    },
                    onRequestCrop = isCropRequested,
                )
            }
        }
    }
    CompleteButton { onCropRequestChange(true) }
}

@Composable
fun ImagePreview(
    selectedImageUri: Uri?,
    selectedImageUris: List<Uri>,
    croppedBitmapList: Map<String, ImageBitmap>,
    selectKey: String,
    onImageSelect: (Uri, String) -> Unit
) {
    if (croppedBitmapList.isNotEmpty()) {
        Image(
            modifier = Modifier.size(400.dp),
            bitmap = croppedBitmapList[selectKey]!!,
            contentDescription = "",
            contentScale = ContentScale.Fit
        )
    } else {
        selectedImageUri?.let { uri ->
            uriToImageBitmap(LocalContext.current, uri)?.let { imageBitmap ->
                Image(
                    modifier = Modifier.size(400.dp),
                    bitmap = imageBitmap,
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
            itemsIndexed(selectedImageUris) { index, uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            onImageSelect(uri, index.toString())
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun CompleteButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
            .fillMaxWidth()
            .padding(12.dp)
            .height(30.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "완료", style = TextStyle(color = Color.White))
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