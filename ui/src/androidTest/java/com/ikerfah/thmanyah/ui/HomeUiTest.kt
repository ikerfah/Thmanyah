package com.ikerfah.thmanyah.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.model.SectionContent
import com.ikerfah.thmanyah.domain.model.SectionType
import com.ikerfah.thmanyah.ui.home.HomeContent
import com.ikerfah.thmanyah.ui.home.HomeUiState
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            HomeContent(
                state = HomeUiState(isLoading = true),
                onSelectedContentTypeChange = {},
                searchQuery = "",
                onSearchQueryChange = {},
                searchResults = emptyList(),
                loadMoreItems = {},
                isRefreshing = false,
                onRefresh = {}
            )
        }

        composeTestRule.onNodeWithTag("homeContentLoading").assertIsDisplayed()
    }

    @Test
    fun showsErrorMessage_whenError() {
        composeTestRule.setContent {
            HomeContent(
                state = HomeUiState(throwable = Throwable("Network error")),
                onSelectedContentTypeChange = {},
                searchQuery = "",
                onSearchQueryChange = {},
                searchResults = emptyList(),
                loadMoreItems = {},
                isRefreshing = false,
                onRefresh = {}
            )
        }

        composeTestRule.onNodeWithTag("ErrorText").assertIsDisplayed()
    }

    @Test
    fun showsSections_whenDataLoaded() {
        val sections = listOf(
            Section(
                name = "Name of a movie",
                type = SectionType.Square,
                contentType = ContentType.Podcast,
                order = 1,
                items = listOf(
                    SectionContent("1", "Batman", "url1", 1, 300)
                )
            )
        )

        composeTestRule.setContent {
            HomeContent(
                state = HomeUiState(
                    _sections = sections,
                    selectedContentType = null
                ),
                onSelectedContentTypeChange = {},
                searchQuery = "",
                onSearchQueryChange = {},
                searchResults = emptyList(),
                loadMoreItems = {},
                isRefreshing = false,
                onRefresh = {}
            )
        }

        composeTestRule.onNodeWithText("Name of a movie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Podcast").assertIsDisplayed()
    }

    @Test
    fun onlySectionsForSelectedCategory_areDisplayed() {
        val podcast = ContentType.Podcast

        val sections = listOf(
            Section(
                name = "Name of a movie",
                type = SectionType.Square,
                contentType = ContentType.Podcast,
                order = 1,
                items = listOf(
                    SectionContent("1", "Batman", "url1", 1, 300)
                )
            ),
            Section(
                name = "Name of a movie",
                type = SectionType.Square,
                contentType = ContentType.Episode,
                order = 1,
                items = listOf(
                    SectionContent("2", "Batman 2", "url2", 2, 300)
                )
            )
        )

        composeTestRule.setContent {
            HomeContent(
                state = HomeUiState(
                    _sections = sections,
                    selectedContentType = podcast
                ),
                onSelectedContentTypeChange = {},
                searchQuery = "",
                onSearchQueryChange = {},
                searchResults = emptyList(),
                loadMoreItems = {},
                isRefreshing = false,
                onRefresh = {}
            )
        }

        composeTestRule.onNodeWithText("Podcast").assertIsSelected()
        composeTestRule.onNodeWithText("Episode").assertIsNotSelected()
        composeTestRule.onNodeWithText("Batman").assertIsDisplayed()
        composeTestRule.onNodeWithText("Batman 2").assertIsNotDisplayed()
    }

    @Test
    fun scrollingToBottom_triggersPagination() {
        var loadMoreCalled = false
        val sections = (1..20).map {
            Section("Section $it", ContentType.Episode, SectionType.Square, it, emptyList())
        }

        composeTestRule.setContent {
            HomeContent(
                state = HomeUiState(
                    _sections = sections,
                ),
                onSelectedContentTypeChange = {},
                searchQuery = "",
                onSearchQueryChange = {},
                searchResults = emptyList(),
                loadMoreItems = { loadMoreCalled = true },
                isRefreshing = false,
                onRefresh = {}
            )
        }

        // Scroll to the last item
        composeTestRule.onNodeWithTag("SectionsList")
            .performScrollToIndex(sections.lastIndex)

        Assert.assertTrue(loadMoreCalled)
    }
}