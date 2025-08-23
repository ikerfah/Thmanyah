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
}