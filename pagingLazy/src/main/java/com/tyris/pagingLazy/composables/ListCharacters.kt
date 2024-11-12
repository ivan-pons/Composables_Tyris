package com.tyris.pagingLazy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyris.domain.model.CharacterBO
import com.tyris.pagingLazy.characters.ErrorMessage
import com.tyris.pagingLazy.characters.LoadingNextPageItem
import com.tyris.pagingLazy.characters.PageLoader
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ListCharacters(
    characters: StateFlow<PagingData<CharacterBO>>,
    onCharacterClicked: (CharacterBO) -> Unit
) {
    val charactersPagingItems: LazyPagingItems<CharacterBO> = characters.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyGridState
    ) {
       items(
           count = charactersPagingItems.itemCount,
           key = {
               charactersPagingItems[it]?.id ?: it
           }){ index ->
           val character = charactersPagingItems[index] ?: return@items
           CharacterListItem(
               character = character,
               onItemClick = {
                   onCharacterClicked(character)
               },
               size = DpSize(60.dp, 60.dp)
           )
       }

        charactersPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item() { PageLoader(
                        modifier = Modifier
                            .fillMaxSize()
                    ) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = charactersPagingItems.loadState.refresh as LoadState.Error
                    item() {
                        ErrorMessage(
                            modifier = Modifier.fillMaxSize(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item() { LoadingNextPageItem(modifier = Modifier) }
                }

                loadState.append is LoadState.Error -> {
                    val error = charactersPagingItems.loadState.append as LoadState.Error
                    item() {
                        ErrorMessage(
                            modifier = Modifier,
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }

    LaunchedEffect(charactersPagingItems) {
        snapshotFlow { charactersPagingItems.itemCount }.collect { count ->
            if (count in 1..30) {
                lazyGridState.scrollToItem(0)
            }
        }
    }
}