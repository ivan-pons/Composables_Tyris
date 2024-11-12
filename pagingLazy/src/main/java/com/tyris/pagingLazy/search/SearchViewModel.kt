package com.tyris.pagingLazy.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tyris.domain.model.CharacterBO
import com.tyris.domain.repositories.CharacterRepository
import com.tyris.pagingLazy.search.SearchActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel (
    private val characterRepository: CharacterRepository
): ViewModel() {


    private val _searchState = MutableStateFlow<PagingData<CharacterBO>>(PagingData.empty())
    val searchState: StateFlow<PagingData<CharacterBO>> = _searchState

    fun onAction(action: SearchActions){
        when(action){
            is SearchActions.CharacterClicked -> {}
            is SearchActions.SearchQueryClicked -> {
                search(action.query)
            }
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            characterRepository.searchCharacters(query)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _searchState.value = pagingData
                }
        }
    }

}