@file:OptIn(ExperimentalMaterial3Api::class)

package com.ikerfah.thmanyah.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikerfah.thmanyah.domain.model.ContentType
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.domain.model.SectionContent
import com.ikerfah.thmanyah.domain.model.SectionType
import com.ikerfah.thmanyah.ui.components.GridItem
import com.ikerfah.thmanyah.ui.components.GridItemSizes
import com.ikerfah.thmanyah.ui.components.Queue
import com.ikerfah.thmanyah.ui.components.Square
import com.ikerfah.thmanyah.ui.components.SquareBig
import com.ikerfah.thmanyah.ui.theme.ThmanyahTheme
import com.ikerfah.thmanyah.ui.theme.highlightedBackground
import com.ikerfah.thmanyah.ui.theme.sectionHeader
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(),
) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    val searchState by searchViewModel.query
    val searchResults by searchViewModel.results.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        homeViewModel.performAction(AppIntent.LoadData)
    }
    HomeContent(
        state = state,
        onSelectedContentTypeChange = { homeViewModel.performAction(AppIntent.SelectContentType(it)) },
        searchQuery = searchState,
        onSearchQueryChange = searchViewModel::onQueryChange,
        searchResults = searchResults,
        isRefreshing = state.isRefreshing,
        loadMoreItems = {
            homeViewModel.performAction(AppIntent.LoadMoreItems)
        },
        onRefresh = {
            homeViewModel.performAction(AppIntent.Refresh)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeUiState,
    onSelectedContentTypeChange: (ContentType) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchResults: List<Section>,
    loadMoreItems: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppBarWithSearch(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                searchResults = searchResults,
            )
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary
    ) { padding ->
        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary) }
        } else if (state.throwable != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error loading data")
                Button(
                    onClick = onRefresh,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(text = "Retry")
                }
            }
        } else {
            SectionsList(
                sections = state.sections,
                contentTypes = state.contentTypes,
                selectedContentType = state.selectedContentType,
                onSelectedContentTypeChange = onSelectedContentTypeChange,
                loadMoreItems = loadMoreItems,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
internal fun SectionsList(
    sections: List<Section>,
    contentTypes: List<ContentType>,
    selectedContentType: ContentType?,
    onSelectedContentTypeChange: (ContentType) -> Unit,
    loadMoreItems: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenWidth =
        with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    val itemWidth = screenWidth * 0.7f
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(modifier.fillMaxSize()) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(contentTypes) { contentType ->
                        CategoryItem(
                            name = contentType.name,
                            isSelected = selectedContentType == contentType,
                            onClick = {
                                onSelectedContentTypeChange(contentType)
                            }
                        )
                    }
                }
            }
            items(sections) { section ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(name = section.name)
                    when (section.type) {
                        SectionType.Square -> {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(section.items) { sectionContent ->
                                    Square(
                                        imageUrl = sectionContent.imageUrl,
                                        title = sectionContent.title,
                                        durationInSeconds = sectionContent.durationInSeconds,
                                        releaseDate = sectionContent.releaseDate,
                                    )
                                }
                            }
                        }

                        SectionType.BigSquare ->
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(section.items) { sectionContent ->
                                    SquareBig(
                                        imageUrl = sectionContent.imageUrl,
                                        title = sectionContent.title
                                    )
                                }
                            }

                        SectionType.TwoLinesGrid -> {
                            LazyHorizontalGrid(
                                rows = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                // LazyHorizontalGrid's height should be bound by parent.
                                // We have two rows, each has max height of GridItemSizes.maxHeight
                                // Total height will be the sum height of the two + some paddings
                                modifier = Modifier.heightIn(max = GridItemSizes.maxHeight.times(2))
                            ) {
                                items(section.items) { sectionContent ->
                                    GridItem(
                                        imageUrl = sectionContent.imageUrl,
                                        title = sectionContent.title,
                                        durationInSeconds = sectionContent.durationInSeconds,
                                        releaseDate = sectionContent.releaseDate,
                                        modifier = Modifier.width(itemWidth)
                                    )
                                }
                            }
                        }

                        SectionType.Queue -> {

                            val itemsToShow = if (section.items.isNotEmpty()) {
                                // We only show the first 4 items of the queue
                                section.items.take(4)
                            } else {
                                emptyList()
                            }
                            Queue(
                                imagesUrl = itemsToShow.map { it.imageUrl },
                                title = itemsToShow.lastOrNull()?.title
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                loadMoreItems()
            }
        }
    }
}

@Composable
private fun SectionHeader(
    name: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                bottom = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.sectionHeader,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun CategoryItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        onClick = onClick,
        label = {
            Text(name)
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = if (isSelected) {
                MaterialTheme.colorScheme.highlightedBackground
            } else {
                MaterialTheme.colorScheme.surface
            },
            selectedLabelColor = if (isSelected) {
                Color.White
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        modifier = modifier
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HomeContentSuccessPreview() {
    ThmanyahTheme {
        HomeContent(
            state = HomeUiState(
                _sections = listOf(
                    Section(
                        name = "Section 1",
                        type = SectionType.Square,
                        order = 1,
                        contentType = ContentType.Podcast,
                        items = listOf(
                            SectionContent(
                                id = "id1",
                                title = "Content 1",
                                imageUrl = null,
                                priority = 1,
                                durationInSeconds = 300,
                                releaseDate = LocalDateTime.now()
                            ),
                            SectionContent(
                                id = "id2",
                                title = "Content 2",
                                imageUrl = null,
                                priority = 2,
                                durationInSeconds = 500,
                                releaseDate = LocalDateTime.now().minusHours(3)
                            )
                        )
                    ),
                    Section(
                        name = "Section 1",
                        type = SectionType.BigSquare,
                        order = 2,
                        contentType = ContentType.Podcast,
                        items = listOf(
                            SectionContent(
                                id = "id1",
                                title = "Content 1",
                                imageUrl = null,
                                priority = 1,
                                durationInSeconds = 100
                            ),
                            SectionContent(
                                id = "id2",
                                title = "Content 2",
                                imageUrl = null,
                                priority = 2,
                                durationInSeconds = 45
                            )
                        )
                    ),
                    Section(
                        name = "Section 1",
                        type = SectionType.Queue,
                        order = 3,
                        contentType = ContentType.AudioBook,
                        items = listOf(
                            SectionContent(
                                id = "id1",
                                title = "Content 1",
                                imageUrl = null,
                                priority = 1,
                                durationInSeconds = 500
                            ),
                            SectionContent(
                                id = "id2",
                                title = "Content 2",
                                imageUrl = null,
                                priority = 2,
                                durationInSeconds = 200
                            )
                        )
                    )
                ),
                selectedContentType = ContentType.Podcast
            ),
            onSelectedContentTypeChange = {},
            searchQuery = "",
            isRefreshing = false,
            onSearchQueryChange = {},
            loadMoreItems = {},
            onRefresh = {},
            searchResults = listOf(),
        )
    }
}