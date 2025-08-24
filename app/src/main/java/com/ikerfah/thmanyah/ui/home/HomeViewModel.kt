package com.ikerfah.thmanyah.ui.home

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeSectionsUseCase: GetHomeSectionsUseCase
) : ViewModel() {
    // Pagination
    private var nextPage: String? = null
    private var isLoading = false

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private fun loadInitialData(isRefreshing: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isRefreshing = isRefreshing) }
            try {
                val response = getHomeSectionsUseCase(page = 1)
                nextPage = response.pagination?.nextPage
                _state.update {
                    it.copy(
                        _sections = response.sections,
                        isLoading = false,
                        isRefreshing = false,
                        throwable = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(throwable = e, isLoading = false, isRefreshing = false) }
            }
        }
    }

    fun performAction(intent: AppIntent) {
        when (intent) {
            AppIntent.LoadData -> loadInitialData(isRefreshing = false)
            AppIntent.Refresh -> loadInitialData(isRefreshing = true)

            is AppIntent.SelectContentType -> _state.update { homeUiState ->
                homeUiState.copy(
                    // deselect if the same is tapped again
                    selectedContentType = intent.contentType.takeUnless { homeUiState.selectedContentType == intent.contentType }
                )
            }

            AppIntent.LoadMoreItems -> {
                if (isLoading || nextPage.isNullOrBlank()) return
                isLoading = true

                viewModelScope.launch {
                    val pageNumber = nextPage?.toUri()?.getQueryParameter("page")?.toIntOrNull()
                    val response = getHomeSectionsUseCase(page = pageNumber)

                    _state.update { it.copy(_sections = it._sections + response.sections) }
                    val newNextPage = response.pagination?.nextPage
                    nextPage =
                        if (newNextPage != nextPage) newNextPage else null // There is a problem with the api, when you request page 2, the pagination next page is 2 instead of 3
                    isLoading = false
                }
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val throwable: Throwable? = null,
    val selectedContentType: ContentType? = null,
    internal val _sections: List<Section> = emptyList(),
) {
    val contentTypes: List<ContentType> = _sections.map { it.contentType }.toSet().toList()

    val sections: List<Section> = _sections.filter { selectedContentType == null || it.contentType == selectedContentType }
}

sealed interface AppIntent {
    data object LoadData : AppIntent
    data object Refresh : AppIntent
    data class SelectContentType(val contentType: ContentType) : AppIntent
    data object LoadMoreItems : AppIntent
}