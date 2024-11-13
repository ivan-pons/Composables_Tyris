package com.tyris.pagingLazy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T: Any> CharacterGrid(
    elements: StateFlow<PagingData<T>>,
    content: @Composable (T) -> Unit,
    columns: GridCells,
    modifier: Modifier = Modifier,
    onKey: ((item: T) -> Any)? = null,
    verticalArrangement: Arrangement. Vertical = Arrangement.spacedBy(8.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceAround
) {
    val elementsPaging: LazyPagingItems<T> = elements.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        state = lazyGridState
    ) {
       items(
           count = elementsPaging.itemCount,
           key = {
               if(onKey != null){
                   elementsPaging[it]?.let { onKey(it)} ?: it
               } else {
                   it
               }
           }){ index ->
           val element = elementsPaging[index] ?: return@items
           content(element)
       }

        elementsPaging.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) { PageLoader(
                        modifier = Modifier
                            .fillMaxSize()
                    ) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = elementsPaging.loadState.refresh as LoadState.Error
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorMessage(
                            modifier = Modifier.fillMaxSize(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) { LoadingNextPageItem(modifier = Modifier) }
                }

                loadState.append is LoadState.Error -> {
                    val error = elementsPaging.loadState.append as LoadState.Error
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorMessage(
                            modifier = Modifier,
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = elementsPaging.loadState.refresh) {
        when(elementsPaging.loadState.refresh){
            is LoadState.NotLoading -> {
                lazyGridState.scrollToItem(0, scrollOffset = 0)
            }
            else -> {
                //Do nothing
            }
        }
    }
}