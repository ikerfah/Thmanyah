@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ikerfah.thmanyah.domain

import app.cash.turbine.test
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.model.SectionType
import com.ikerfah.thmanyah.domain.repository.AppRepository
import com.ikerfah.thmanyah.domain.usecase.SearchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.atMost
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SearchUseCaseTest {
    private lateinit var repository: AppRepository
    private lateinit var searchUseCase: SearchUseCase

    @Before
    fun setUp() {
        repository = mock()
        searchUseCase = SearchUseCase(repository)
    }

    @Test
    fun `empty query returns empty list`() = runTest {
        val queryFlow = MutableStateFlow("")

        searchUseCase(queryFlow).test {
            advanceTimeBy(300)
            assertEquals(emptyList<Section>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `non-empty query calls repository after debounce`() = runTest {
        val expected = listOf(
            Section(
                name = "result",
                contentType = ContentType.Podcast,
                type = SectionType.Square,
                order = 1,
                items = emptyList()
            )
        )
        whenever(repository.search("hello")).thenReturn(expected)

        val queryFlow = MutableStateFlow("hello")

        searchUseCase(queryFlow).test {
            advanceTimeBy(199) // still within debounce (200)
            expectNoEvents()

            advanceTimeBy(10) // cross the debounce boundary
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `debounce drops intermediate values`() = runTest {
        val expected = listOf(
            Section(
                name = "result",
                contentType = ContentType.Podcast,
                type = SectionType.Square,
                order = 1,
                items = emptyList()
            )
        )

        whenever(repository.search("final")).thenReturn(expected)

        val queryFlow = MutableStateFlow("f")

        searchUseCase(queryFlow).test {
            queryFlow.value = "fi"
            queryFlow.value = "fin"
            queryFlow.value = "final"

            advanceTimeBy(250)

            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
            verify(repository, never()).search("fi")
            verify(repository, never()).search("fin")
            verify(repository, atMost(1)).search("final")
        }
    }
}