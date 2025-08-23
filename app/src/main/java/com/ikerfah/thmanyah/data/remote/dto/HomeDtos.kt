package com.ikerfah.thmanyah.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeSectionDto(
    @Json(name = "sections") val sections: List<SectionDto>,
    @Json(name = "pagination") val pagination: PaginationDto? = null
)

@JsonClass(generateAdapter = true)
data class PaginationDto(
    @Json(name = "next_page") val nextPage: String? = null,
    @Json(name = "total_pages") val totalPages: Int? = null
)

@JsonClass(generateAdapter = true)
data class SectionDto(
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "content_type") val contentType: String,
    @Json(name = "order") val order: Int? = null,
    @Json(name = "content") val content: List<ContentDto>
)

@JsonClass(generateAdapter = true)
data class ContentDto(
    @Json(name = "podcast_id") val podcastId: String? = null,
    @Json(name = "name") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "priority") val priority: Int?,
    @Json(name = "podcastPriority") val podcastPriority: Int?,

    // Episode
    @Json(name = "episode_id") val episodeId: String? = null,

    // Audiobook
    @Json(name = "audiobook_id") val audiobookId: String? = null,

    // Article
    @Json(name = "article_id") val articleId: String? = null
)