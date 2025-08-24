package com.ikerfah.thmanyah.data.repository

import com.ikerfah.thmanyah.data.mapper.toDomain
import com.ikerfah.thmanyah.data.remote.ApiService
import com.ikerfah.thmanyah.data.remote.SearchService
import com.ikerfah.thmanyah.data.remote.dto.SearchSectionDto
import com.ikerfah.thmanyah.domain.model.HomeSection
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.repository.AppRepository

class AppRepositoryImpl(
    private val apiService: ApiService,
    private val searchService: SearchService
) : AppRepository {
    override suspend fun getHomeSections(page: Int?): HomeSection {
        return apiService
            .getHomeSections(page = page)
            .toDomain()
    }

    override suspend fun search(query: String): List<Section> {
        return searchService.search(query)
            .sections
            .map(SearchSectionDto::toDomain)
    }
}