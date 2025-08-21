package com.ikerfah.thmanyah.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeSectionsUseCase: GetHomeSectionsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _state.value = HomeUiState.Loading
            try {
                _state.value =
                    HomeUiState.Success(sections = getHomeSectionsUseCase(page = 1)) // TODO: Do something about the pagination
            } catch (e: Exception) {
                _state.value = HomeUiState.Error(e)
            }
        }
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val sections: List<Section>) : HomeUiState()
    data class Error(val throwable: Throwable) : HomeUiState()
}