package com.ikerfah.thmanyah.domain.repository

import com.ikerfah.thmanyah.domain.model.HomeSection
import com.ikerfah.thmanyah.domain.model.Section

interface AppRepository {
    suspend fun getHomeSections(page: Int? = null): HomeSection
    suspend fun search(query: String): List<Section>
}