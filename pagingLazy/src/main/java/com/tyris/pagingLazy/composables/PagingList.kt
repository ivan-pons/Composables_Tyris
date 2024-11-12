package com.tyris.pagingLazy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyris.pagingLazy.characters.ErrorMessage
import com.tyris.pagingLazy.characters.LoadingNextPageItem
import com.tyris.pagingLazy.characters.PageLoader
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T: Any> PagingList(
    elements: StateFlow<PagingData<T>>,
    content: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    onKey: ((item: T) -> Any)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    searchedQuery: String = ""
) {
    val elementsPaging: LazyPagingItems<T> = elements.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        state = lazyListState
    ) {
       items(
           count = elementsPaging.itemCount,
           key = {
               if(onKey != null){
                   elementsPaging[it]?.let { onKey(it)} ?: it
               } else {
                   it
               }
           }
       ){ index ->
           val element = elementsPaging[index] ?: return@items
           content(element)
       }

        elementsPaging.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item() { PageLoader(
                        modifier = Modifier
                            .fillMaxSize()
                    ) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = elementsPaging.loadState.refresh as LoadState.Error
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
                    val error = elementsPaging.loadState.append as LoadState.Error
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

    LaunchedEffect(searchedQuery, elementsPaging) {
        lazyListState.scrollToItem(0)
    }
}