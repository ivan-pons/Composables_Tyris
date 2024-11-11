package com.tyris.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterBO(
    val created: String,
    val episodesId: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: ShortLocationBO,
    val name: String,
    val origin: ShortLocationBO,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

@Serializable
data class ShortLocationBO(
    val name: String,
    val id: String,
)

