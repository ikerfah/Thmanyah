package com.ikerfah.thmanyah.data.mapper

import com.ikerfah.thmanyah.data.remote.dto.SectionDto
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.model.SectionContent
import com.ikerfah.thmanyah.domain.model.SectionType


private fun toSectionType(raw: String): SectionType = when (raw.trim().lowercase()) {
    "square" -> SectionType.Square
    "2_lines_grid" -> SectionType.TwoLinesGrid
    "big_square", "big square" -> SectionType.BigSquare
    "queue" -> SectionType.Queue
    else -> SectionType.Square // fallback
}

private fun toContentType(raw: String): ContentType = when (raw.trim().lowercase()) {
    "podcast" -> ContentType.Podcast
    "episode" -> ContentType.Episode
    "audio_book" -> ContentType.AudioBook
    "audio_article" -> ContentType.AudioArticle
    else -> ContentType.Podcast // Is this the right fallback?
}

fun SectionDto.toDomain(): Section {
    val contentType = toContentType(contentType)
    return Section(
        name = name,
        contentType = contentType,
        type = toSectionType(type),
        order = order ?: Int.MAX_VALUE, // if order is not defined, make it last
        items = content.mapNotNull {
            SectionContent(
                id = when (contentType) {
                    ContentType.Podcast -> it.podcastId ?: return@mapNotNull null
                    ContentType.Episode -> it.episodeId ?: return@mapNotNull null
                    ContentType.AudioBook -> it.audiobookId ?: return@mapNotNull null
                    ContentType.AudioArticle -> it.articleId ?: return@mapNotNull null
                },
                title = it.name,
                imageUrl = it.avatarUrl,
                priority = it.priority ?: it.podcastPriority ?: Int.MAX_VALUE,
            )
        }
    )
}

