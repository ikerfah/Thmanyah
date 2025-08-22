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
                _state.update { it.copy(sections = getHomeSectionsUseCase(page = 1)) } // TODO: Do something about the pagination
            } catch (e: Exception) {
                _state.update { it.copy(throwable = e) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun performAction(intent: AppIntent) {
        when (intent) {
            AppIntent.LoadData -> refresh()
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val sections: List<Section> = emptyList(),
    val throwable: Throwable? = null
)

sealed interface AppIntent {
    data object LoadData : AppIntent
}