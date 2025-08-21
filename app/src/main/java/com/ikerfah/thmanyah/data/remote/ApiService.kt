package com.ikerfah.thmanyah.data.remote

import com.ikerfah.thmanyah.data.remote.dto.HomeSectionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("home_sections")
    suspend fun getHomeSections(@Query("page") page: Int? = null): HomeSectionDto
}