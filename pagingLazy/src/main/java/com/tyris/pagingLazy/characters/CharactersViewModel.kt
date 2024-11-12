package com.tyris.pagingLazy.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tyris.domain.model.CharacterBO
import com.tyris.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharactersViewModel (
    private val characterRepository: CharacterRepository
): ViewModel() {

    private val _charactersState = MutableStateFlow<PagingData<CharacterBO>>(PagingData.empty())
    val charactersState: StateFlow<PagingData<CharacterBO>> = _charactersState


    init {
        viewModelScope.launch {
            characterRepository.getAllCharacters()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                _charactersState.value = pagingData
            }
        }
    }

    fun onAction(action: CharactersActions){

    }
}