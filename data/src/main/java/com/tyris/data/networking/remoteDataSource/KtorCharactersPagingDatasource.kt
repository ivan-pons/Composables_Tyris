package com.tyris.data.networking.remoteDataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tyris.data.networking.get
import com.tyris.data.networking.model.CharacterDto
import com.tyris.data.networking.model.PageDto
import io.ktor.client.HttpClient
import java.io.IOException
import com.tyris.domain.util.Result as SafeResult

class KtorCharactersPagingDatasource(
    private val httpClient: HttpClient
): PagingSource<Int, CharacterDto>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = httpClient.get<PageDto<CharacterDto>>(
                route = "/api/character",
                queryParameters = mapOf(
                    "page" to (currentLoadingPageKey).toString()
                )
            )
            when(response){
                is SafeResult.Error -> {
                    return LoadResult.Error(Exception(response.error.name))
                }
                is SafeResult.Success -> {
                    val page = response.data
                    val prevKey = if (page.info?.prev == null) null else currentLoadingPageKey - 1
                    val nextKey = if (page.info?.next == null) null else currentLoadingPageKey + 1

                    return LoadResult.Page(
                    data = page.results ?: emptyList(),
                    prevKey = prevKey,
                    nextKey = nextKey
                    )
                }
            }

        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        }

    }
}