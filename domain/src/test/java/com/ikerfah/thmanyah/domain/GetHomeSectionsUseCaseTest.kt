package com.ikerfah.thmanyah.domain

import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.HomeSection
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.model.SectionContent
import com.ikerfah.thmanyah.domain.model.SectionType
import com.ikerfah.thmanyah.domain.repository.AppRepository
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetHomeSectionsUseCaseTest {

    private lateinit var repository: AppRepository
    private lateinit var getHomeSectionsUseCase: GetHomeSectionsUseCase

    @Before
    fun setUp() {
        repository = mock()
        getHomeSectionsUseCase = GetHomeSectionsUseCase(repository)
    }

    @Test
    fun `invoke should sort sections by order`() = runTest {
        val unsortedSections = listOf(
            Section(
                name = "B",
                contentType = ContentType.Podcast,
                type = SectionType.Square,
                order = 2,
                items = emptyList()
            ),
            Section(
                name = "A",
                contentType = ContentType.AudioBook,
                type = SectionType.BigSquare,
                order = 1,
                items = emptyList()
            )
        )

        whenever(repository.getHomeSections(page = null))
            .thenReturn(HomeSection(unsortedSections,null))

        val result = getHomeSectionsUseCase()

        assertEquals(listOf(unsortedSections[1], unsortedSections[0]), result.sections)
        verify(repository).getHomeSections(page = null)
    }

    @Test
    fun `invoke should sort items inside each section by priority`() = runTest {
        val items = listOf(
            SectionContent("1", "Item1", null, priority = 3, durationInSeconds = 120),
            SectionContent("2", "Item2", null, priority = 1, durationInSeconds = 60),
            SectionContent("3", "Item3", null, priority = 2, durationInSeconds = 180)
        )

        val section = Section(
            name = "id1",
            contentType = ContentType.Episode,
            type = SectionType.Queue,
            order = 1,
            items = items
        )

        whenever(repository.getHomeSections(page = 1)).thenReturn(
            HomeSection(listOf(section), null))

        val result = getHomeSectionsUseCase(page = 1)

        val sortedItems = result.sections.first().items
        assertEquals(listOf(1, 2, 3), sortedItems.map { it.priority })
    }

    @Test
    fun `invoke should handle empty list`() = runTest {
        whenever(repository.getHomeSections(page = null)).thenReturn(HomeSection(emptyList(), null))

        val result = getHomeSectionsUseCase()

        assertEquals(emptyList<Section>(), result.sections)
    }
}
