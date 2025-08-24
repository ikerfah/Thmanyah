package com.ikerfah.thmanyah.domain.usecase

import com.ikerfah.thmanyah.domain.model.HomeSection
import com.ikerfah.thmanyah.domain.repository.AppRepository

class GetHomeSectionsUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(page: Int? = null): HomeSection {
        return repository.getHomeSections(page = page).let { homeSection ->
            homeSection.copy(
                sections = homeSection
                    .sections
                    .sortedBy { it.order }
                    .map { section ->
                        section.copy(items = section.items.sortedBy { it.priority })
                    }
            )
        }
    }
}