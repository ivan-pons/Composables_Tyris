package com.tyris.pagingLazy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T : Any> PagingGrid(
    elements: StateFlow<PagingData<T>>,
    content: @Composable (T) -> Unit,
    columns: GridCells,
    modifier: Modifier = Modifier,
    onKey: ((item: T) -> Any)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceAround,
    loadingNextContent: @Composable (() -> Unit) = { LoadingNextPageItem(modifier = Modifier) },
    errorContent: @Composable (() -> Unit)? = null,
    pageLoader: @Composable (() -> Unit) = { PageLoader(modifier = Modifier.fillMaxSize()) }
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
                if (onKey != null) {
                    elementsPaging[it]?.let { onKey(it) } ?: it
                } else {
                    it
                }
            }) { index ->
            val element = elementsPaging[index] ?: return@items
            content(element)
        }

        elementsPaging.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        pageLoader()
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = elementsPaging.loadState.refresh as LoadState.Error
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        if (errorContent != null) {
                            errorContent()
                        } else {
                            ErrorMessage(
                                modifier = Modifier.fillMaxSize(),
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() })
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        loadingNextContent()
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = elementsPaging.loadState.append as LoadState.Error
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        if (errorContent != null) {
                            errorContent()
                        } else {
                            ErrorMessage(
                                modifier = Modifier.fillMaxSize(),
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() })
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = elementsPaging.loadState.refresh) {
        when (elementsPaging.loadState.refresh) {
            is LoadState.NotLoading -> {
                lazyGridState.scrollToItem(0, scrollOffset = 0)
            }

            else -> {
                //Do nothing
            }
        }
    }
}

@Composable
fun PageLoader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Loading",
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CircularProgressIndicator(Modifier.padding(top = 10.dp))
    }
}

@Composable
fun LoadingNextPageItem(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Retry")
        }
    }
}