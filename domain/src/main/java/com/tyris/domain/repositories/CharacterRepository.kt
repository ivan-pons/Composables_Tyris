package com.tyris.domain.repositories

import androidx.paging.PagingData
import com.tyris.domain.model.CharacterBO
import com.tyris.domain.util.DataError
import kotlinx.coroutines.flow.Flow
import com.tyris.domain.util.Result as SafeResult

interface CharacterRepository {
    suspend fun getAllCharacters(): Flow<PagingData<CharacterBO>>
    suspend fun searchCharacters(query: String): Flow<PagingData<CharacterBO>>
    suspend fun getCharacter(characterId: Int): SafeResult<CharacterBO, DataError.Network>
    suspend fun getMultipleCharacters(characterIds: List<String>): SafeResult<List<CharacterBO>, DataError.Network>
}