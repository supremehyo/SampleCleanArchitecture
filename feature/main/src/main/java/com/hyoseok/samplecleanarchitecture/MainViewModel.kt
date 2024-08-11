package com.hyoseok.samplecleanarchitecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
    private val _screenState = MutableStateFlow(MainPath.Home.path)
    val screenState = _screenState.asStateFlow()

    fun updateScreenState(state: String) {
        viewModelScope.launch {
            _screenState.emit(state)
        }
    }
}