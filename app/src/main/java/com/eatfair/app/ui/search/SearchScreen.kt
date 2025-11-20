package com.eatfair.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.app.ui.theme.pinkVerticalGradient
import com.eatfair.shared.model.search.SearchResultDto

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onBackClick: () -> Unit = {},
    onRecentSearchClick: (String) -> Unit = {},
    onCuisineClick: (String) -> Unit = {},
    onMicClick: () -> Unit = {},
    onSearchResultClick: (SearchResultDto) -> Unit
) {
    val query by searchViewModel.searchQuery.collectAsState()
    val results by searchViewModel.searchResults.collectAsState()
    val recentSearches by searchViewModel.recentSearches.collectAsState()
    val popularCuisines by searchViewModel.popularCuisines.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pinkVerticalGradient())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                .background(Color(0xFFFFF5F7))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            AdvancedSearchBarMaterial3(
                searchQuery = query,
                onSearchQueryChange = {
                    searchViewModel.onSearchQueryChange(it)
                },
                onBackClick = onBackClick,
                onMicClick = onMicClick,
                modifier = Modifier.focusRequester(focusRequester)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            // Recent Searches Section
            if (results.isEmpty() && query.isBlank()) {
                item {
                    Text(
                        text = "Recent Searches",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        recentSearches.forEach { search ->
                            SearchChipMaterial3(
                                text = search,
                                onClick = { onRecentSearchClick(search) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Popular Cuisines Section
                    Text(
                        text = "Popular Cuisines",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
//                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        popularCuisines.forEach { cuisine ->
                            SearchChipMaterial3(
                                text = cuisine,
                                onClick = { onCuisineClick(cuisine) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            } else {
                // Search Results
                item {
                    Text(
                        text = "Results for \"$query\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(results) { item ->
                    SearchResultItem(result = item, onSearchResultClick = onSearchResultClick)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun SearchChipMaterial3(
    text: String,
    onClick: () -> Unit
) {
    SuggestionChip(
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Gray
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = Color.White,
            labelColor = Color.Gray
        ),
        border = null,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun SearchResultItem(result: SearchResultDto, onSearchResultClick: (SearchResultDto) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { /* Navigate to detail */ },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = {onSearchResultClick(result)}
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍕", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = result.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.category,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Rating
            Text(
                text = "⭐ ${result.rating}",
                fontSize = 13.sp,
                color = Color.Black
            )
        }
    }
}
