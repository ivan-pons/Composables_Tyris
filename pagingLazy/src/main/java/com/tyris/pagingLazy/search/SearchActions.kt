package com.tyris.pagingLazy.search

import com.tyris.domain.model.CharacterBO

sealed interface SearchActions {
    data class CharacterClicked(val character: CharacterBO) : SearchActions
    data class SearchQueryClicked(val query: String) : SearchActions
}