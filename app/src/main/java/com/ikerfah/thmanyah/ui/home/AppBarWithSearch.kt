package com.ikerfah.thmanyah.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ikerfah.thmanyah.R
import com.ikerfah.thmanyah.domain.model.Section
import com.ikerfah.thmanyah.ui.theme.appBarText

@Composable
fun AppBarWithSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<Section>,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }
    AnimatedContent(
        targetState = isSearch,
        modifier = modifier,
    ) { isSearchState ->
        if (isSearchState) {
            CustomizableSearchBar(
                modifier = modifier,
                query = query,
                onQueryChange = onQueryChange,
                searchResults = searchResults,
                onCloseSearch = {
                    isSearch = false
                }
            )
        } else {
            TopAppBar(
                modifier = modifier,
                onSearchClick = {
                    isSearch = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<Section>,
    onCloseSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter).padding(bottom = 24.dp),
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.primary),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onQueryChange(query)
                    },
                    expanded = true,
                    onExpandedChange = {  },
                    placeholder = {
                        Text("Search...")
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = onCloseSearch
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close search"
                            )
                        }
                    },
                    colors = inputFieldColors(cursorColor = MaterialTheme.colorScheme.onPrimary),
                )
            },
            expanded = true, // State is handed in the parent
            onExpandedChange = {  },
        ) {
            SectionsList(
                sections = searchResults,
                contentTypes = emptyList(),
                selectedContentType = null,
                onSelectedContentTypeChange = {},
                loadMoreItems = {},
                isRefreshing = false,
                onRefresh = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_circle),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Good afternoon, Abdurahman",
                    style = MaterialTheme.typography.appBarText

                )
            }
        },
        actions = {
            IconButton(onSearchClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
            BadgedBox(
                badge = {
                    Badge {
                        Text("4")
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}