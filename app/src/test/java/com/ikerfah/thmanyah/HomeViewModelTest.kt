package com.ikerfah.thmanyah

import app.cash.turbine.test
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.model.SectionType
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import com.ikerfah.thmanyah.ui.home.AppIntent
import com.ikerfah.thmanyah.ui.home.HomeViewModel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var getHomeSectionsUseCase: GetHomeSectionsUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        getHomeSectionsUseCase = mock()
        viewModel = HomeViewModel(getHomeSectionsUseCase)
    }

    @Test
    fun `initial state is empty`() = testScope.runTest {
        val initialState = viewModel.state.value
        assertEquals(false, initialState.isLoading)
        assertTrue(initialState.sections.isEmpty())
        assertNull(initialState.selectedContentType)
        assertNull(initialState.throwable)
    }

    @Test
    fun `performAction LoadData loads sections`() = testScope.runTest {
        val fakeSections = listOf(
            Section(
                name = "id1",
                contentType = ContentType.Podcast,
                type = SectionType.Square,
                order = 1,
                items = listOf()
            ),
        )
        whenever(getHomeSectionsUseCase.invoke(page = 1)).thenReturn(fakeSections)

        viewModel.state.test {
            viewModel.performAction(AppIntent.LoadData)

            // skip initial
            awaitItem()
            // loading state
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            // loaded state
            val loaded = awaitItem()
            assertEquals(fakeSections, loaded.sections)
            assertFalse(loaded.isLoading)

            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `performAction LoadData handles exception`() = testScope.runTest {
        val error = RuntimeException("Network error")
        whenever(getHomeSectionsUseCase.invoke(page = 1)).thenThrow(error)

        viewModel.state.test {
            viewModel.performAction(AppIntent.LoadData)

            // skip initial
            awaitItem()
            // loading
            awaitItem()
            // error state
            val errorState = awaitItem()
            assertEquals(error, errorState.throwable)
            assertFalse(errorState.isLoading)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `performAction SelectContentType filters sections`() = testScope.runTest {
        val fakeSections = listOf(
            Section(
                name = "id1",
                contentType = ContentType.Podcast,
                type = SectionType.Square,
                order = 1,
                items = listOf()
            ),
            Section(
                name = "id2",
                contentType = ContentType.AudioBook,
                type = SectionType.Square,
                order = 2,
                items = listOf()
            ),
            Section(
                name = "id3",
                contentType = ContentType.Episode,
                type = SectionType.Square,
                order = 3,
                items = listOf()
            ),
            Section(
                name = "id4",
                contentType = ContentType.Podcast,
                type = SectionType.Square,
                order = 4,
                items = listOf()
            ),
        )
        whenever(getHomeSectionsUseCase.invoke(page = 1)).thenReturn(fakeSections)

        viewModel.state.test {
            viewModel.performAction(AppIntent.LoadData)
            // skip initial
            awaitItem()
            // Skip loading
            awaitItem()

            assertNull(awaitItem().selectedContentType)
            viewModel.performAction(AppIntent.SelectContentType(ContentType.Podcast))

            var updated = awaitItem()
            assertEquals(ContentType.Podcast, updated.selectedContentType)
            assertEquals(2, updated.sections.size)
            assertTrue(updated.sections.all { it.contentType == ContentType.Podcast })

            // Switch to another content type
            viewModel.performAction(AppIntent.SelectContentType(ContentType.Episode))
            updated = awaitItem()
            assertEquals(ContentType.Episode, updated.selectedContentType)
            assertEquals(1, updated.sections.size)
            assertTrue(updated.sections.all { it.contentType == ContentType.Episode })

            // Select the some content type unselect the selection
            viewModel.performAction(AppIntent.SelectContentType(ContentType.Episode))
            updated = awaitItem()
            assertNull(updated.selectedContentType)
            assertEquals(4, updated.sections.size)

            cancelAndConsumeRemainingEvents()
        }
    }
}