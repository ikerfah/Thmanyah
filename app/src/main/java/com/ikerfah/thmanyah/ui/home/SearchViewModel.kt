@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.ikerfah.thmanyah.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.usecase.SearchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(private val searchUseCase: SearchUseCase) : ViewModel() {
    val query = mutableStateOf("")

    private val _results = MutableStateFlow<List<Section>>(emptyList())
    val results: StateFlow<List<Section>> = _results.asStateFlow()

    init {
        viewModelScope.launch {
            searchUseCase(snapshotFlow { query.value })
                .collectLatest {
                    _results.value = it
                }
        }
    }

    fun onQueryChange(text: String) {
        query.value = text
    }
}