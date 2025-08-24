package com.ikerfah.thmanyah.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    @Json(name = "sections") val sections: List<SearchSectionDto>,
)

@JsonClass(generateAdapter = true)
data class SearchSectionDto(
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "content_type") val contentType: String,
    @Json(name = "order") val order: String? = null,
    @Json(name = "content") val content: List<SearchContentDto>
)

@JsonClass(generateAdapter = true)
data class SearchContentDto(
    @Json(name = "podcast_id") val podcastId: String?,
    @Json(name = "name") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "priority") val priority: String?,
    @Json(name = "duration") val duration: String?,
)