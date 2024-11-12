package com.tyris.pagingLazy.characters

import com.tyris.domain.model.CharacterBO


sealed interface CharactersActions {
    data class CharacterClicked(val character: CharacterBO) : CharactersActions
}