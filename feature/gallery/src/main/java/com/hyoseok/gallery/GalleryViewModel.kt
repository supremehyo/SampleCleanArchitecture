package com.hyoseok.gallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyoseok.samplecleanarchitecture.core.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor() : ViewModel() {
    private val _galleryEventFlow = MutableSharedFlow<GalleryEvent>()
    val galleryEventFlow = _galleryEventFlow.asSharedFlow()

    private fun postGalleryEvent(event : GalleryEvent){
        viewModelScope.launch {
            _galleryEventFlow.emit(event)
        }
    }

    fun updateGalleryScreenState(uris : List<Uri>){
        if(uris.isEmpty()){
            postGalleryEvent(GalleryEvent.GalleryUiEvent(UiState.Loading))
        }else{
            postGalleryEvent(GalleryEvent.GalleryUiEvent(UiState.Success(true)))
        }
    }

    sealed class GalleryEvent {
        data class GalleryUiEvent(val uiState: UiState<Boolean>) : GalleryEvent()
    }
}