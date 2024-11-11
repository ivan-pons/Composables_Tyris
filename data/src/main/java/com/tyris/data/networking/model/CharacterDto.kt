package com.tyris.data.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    @SerialName("created") val created: String?,
    @SerialName("episode") val episode: List<String>?,
    @SerialName("gender") val gender: String?,
    @SerialName("id") val id: Int?,
    @SerialName("image") val image: String?,
    @SerialName("location") val location: ShortLocationDto?,
    @SerialName("name") val name: String?,
    @SerialName("origin") val origin: ShortLocationDto?,
    @SerialName("species") val species: String?,
    @SerialName("status") val status: String?,
    @SerialName("type") val type: String?,
    @SerialName("url") val url: String?
)


