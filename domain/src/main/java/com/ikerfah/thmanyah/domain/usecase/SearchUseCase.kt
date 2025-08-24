package com.ikerfah.thmanyah.domain.usecase

import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.Duration.Companion.milliseconds

class SearchUseCase(private val repository: AppRepository) {
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    operator fun invoke(state: Flow<String>): Flow<List<Section>> {
        return state
            .debounce(200.milliseconds)
            .mapLatest { query ->
                val trimmed = query.trim()
                if (trimmed.isEmpty()) emptyList() else repository.search(trimmed)
            }

    }
}