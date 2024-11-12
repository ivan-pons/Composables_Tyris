package com.tyris.pagingLazy.characters

import com.tyris.domain.model.CharacterBO

data class CharactersState(
    val characters: List<CharacterBO> = emptyList(),
    val isLoading: Boolean = false
)