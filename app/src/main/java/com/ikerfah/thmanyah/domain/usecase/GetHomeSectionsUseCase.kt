package com.ikerfah.thmanyah.domain.usecase

import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.repository.AppRepository

class GetHomeSectionsUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(page: Int? = null): List<Section> =
        repository.getHomeSections(page = page)
            .sortedBy { it.order }
            .map { section ->
                section.copy(items = section.items.sortedBy { it.priority })
            }
}