package com.ikerfah.thmanyah.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeSectionsUseCase: GetHomeSectionsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                _state.update { it.copy(_sections = getHomeSectionsUseCase(page = 1), isLoading = false) } // TODO: Do something about the pagination
            } catch (e: Exception) {
                _state.update { it.copy(throwable = e, isLoading = false) }
            }
        }
    }

    fun performAction(intent: AppIntent) {
        when (intent) {
            AppIntent.LoadData -> refresh()
            is AppIntent.SelectContentType -> _state.update { homeUiState ->
                homeUiState.copy(
                    // deselect if the same is tapped again
                    selectedContentType = intent.contentType.takeUnless { homeUiState.selectedContentType == intent.contentType }
                )
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val selectedContentType: ContentType? = null,
    private val _sections: List<Section> = emptyList(),
) {
    val contentTypes: List<ContentType> = _sections.map { it.contentType }.toSet().toList()

    val sections: List<Section> = _sections.filter { selectedContentType == null || it.contentType == selectedContentType }
}

sealed interface AppIntent {
    data object LoadData : AppIntent
    data class SelectContentType(val contentType: ContentType): AppIntent
}