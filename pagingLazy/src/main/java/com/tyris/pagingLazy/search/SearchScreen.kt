package com.tyris.pagingLazy.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.tyris.domain.model.CharacterBO
import com.tyris.pagingLazy.composables.PagingGrid
import com.tyris.pagingLazy.composables.CharacterGridItem
import com.tyris.pagingLazy.composables.SearchToolbar
import com.tyris.pagingLazy.ui.theme.ComposablesTyrisTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreenRoot(
    onCharacterClicked: (CharacterBO) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    SearchScreen(
        state = viewModel.searchState,
        onAction = { action ->
            when (action) {
                is SearchActions.CharacterClicked -> {
                    onCharacterClicked(action.character)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun SearchScreen(
    state: StateFlow<PagingData<CharacterBO>>,
    onAction: (SearchActions) -> Unit
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
        ) {
            var query by rememberSaveable { mutableStateOf("") }
            SearchToolbar(
                query = query,
                hint = "personaje",
                onQueryChanged = { newQuery ->
                    query = newQuery
                },
                onSearchClicked = {
                    onAction(SearchActions.SearchQueryClicked(query))
                }
            )

            PagingGrid(
                elements = state,
                content = {
                    CharacterGridItem(
                        character = it,
                        onItemClick = {
                            onAction(SearchActions.CharacterClicked(it))
                        },
                        size = DpSize(120.dp, 120.dp)
                    )
                },
                modifier =  Modifier.padding(vertical = 16.dp),
                columns = GridCells.FixedSize(size = 160.dp)
            )

    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    ComposablesTyrisTheme {
        SearchScreen(
            state = MutableStateFlow(PagingData.empty()),
            onAction = {}
        )
    }
}