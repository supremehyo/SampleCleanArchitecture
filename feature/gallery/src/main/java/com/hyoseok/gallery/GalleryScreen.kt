package com.hyoseok.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hyoseok.samplecleanarchitecture.core.designsystem.component.TextButtonComponent
import com.hyoseok.samplecleanarchitecture.core.designsystem.theme.LocalTypography
import com.hyoseok.samplecleanarchitecture.core.model.UiState
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
    var loadComplete by remember { mutableStateOf(false) }
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
            modifier = Modifier.fillMaxSize(),
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

            }
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.size(400.dp),
                contentScale = ContentScale.Crop
            )
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
                    items(selectedImageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    selectedImageUri = uri
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
