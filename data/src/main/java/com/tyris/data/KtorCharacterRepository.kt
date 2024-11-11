package com.tyris.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tyris.data.mappers.toCharacterBO
import com.tyris.data.networking.get
import com.tyris.data.networking.model.CharacterDto
import com.tyris.data.networking.remoteDataSource.KtorCharactersPagingDatasource
import com.tyris.data.networking.remoteDataSource.KtorSearchPagingDatasource
import com.tyris.domain.model.CharacterBO
import com.tyris.domain.repositories.CharacterRepository
import com.tyris.domain.util.DataError
import com.tyris.domain.util.map
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.tyris.domain.util.Result as SafeResult

class KtorCharacterRepository (
    private val ktorCharactersPagingDatasource: KtorCharactersPagingDatasource,
    private val ktorSearchPagingDatasource: KtorSearchPagingDatasource,
    private val httpClient: HttpClient
) : CharacterRepository {

    override suspend fun getAllCharacters(): Flow<PagingData<CharacterBO>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                initialLoadSize = 40
            )
        ) {
            ktorCharactersPagingDatasource
        }.flow.map {
            it.map { characterDto ->
                characterDto.toCharacterBO()
            }
        }
    }

    override suspend fun searchCharacters(query: String): Flow<PagingData<CharacterBO>> {
        ktorSearchPagingDatasource.setQuery(query)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                initialLoadSize = 40
            )
        ) {
            ktorSearchPagingDatasource
        }.flow.map {
            it.map { characterDto ->
                characterDto.toCharacterBO()
            }
        }
    }

    override suspend fun getCharacter(characterId: Int): SafeResult<CharacterBO, DataError.Network> {
        return httpClient.get<CharacterDto>(
            route = "/api/character/$characterId"
        ).map { characterDto ->
           characterDto.toCharacterBO()
        }
    }

    override suspend fun getMultipleCharacters(characterIds: List<String>): SafeResult<List<CharacterBO>, DataError.Network> {
        val characterIdsString = characterIds.joinToString(",")
        return httpClient.get<List<CharacterDto>>(
            route = "/api/character/[$characterIdsString]"
        ).map { characterDtoList ->
            characterDtoList.map { characterDto ->
                characterDto.toCharacterBO()
            }
        }
    }

}