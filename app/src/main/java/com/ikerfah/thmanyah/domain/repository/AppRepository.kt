package com.ikerfah.thmanyah.domain.repository

import com.ikerfah.thmanyah.domain.model.Section

interface AppRepository {
    suspend fun getHomeSections(page: Int? = null): List<Section>
}