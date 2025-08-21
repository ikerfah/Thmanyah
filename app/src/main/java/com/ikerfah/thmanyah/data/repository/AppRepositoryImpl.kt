package com.ikerfah.thmanyah.data.repository

import com.ikerfah.thmanyah.data.mapper.toDomain
import com.ikerfah.thmanyah.data.remote.ApiService
import com.ikerfah.thmanyah.data.remote.dto.SectionDto
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.repository.AppRepository

class AppRepositoryImpl(
    private val apiService: ApiService
) : AppRepository {
    override suspend fun getHomeSections(page: Int?): List<Section> {
        return apiService
            .getHomeSections(page = page)
            .sections
            .map(SectionDto::toDomain)
    }
}