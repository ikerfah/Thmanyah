package com.ikerfah.thmanyah.data.remote

import com.ikerfah.thmanyah.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search")
    suspend fun search(@Query("q") query: String): SearchResponseDto
}