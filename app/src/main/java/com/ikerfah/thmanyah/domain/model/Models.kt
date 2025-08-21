package com.ikerfah.thmanyah.domain.model

data class Section(
    val name: String,
    val type: SectionType,
    val order: Int,
    val items: List<SectionContent>
)

data class SectionContent(
    val id: String,
    val title: String,
    val imageUrl: String?,
)

enum class ContentType { Podcast, Episode, AudioBook, AudioArticle }
enum class SectionType { Square, BigSquare, TwoLinesGrid, Queue }
