package com.tyris.data.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShortLocationDto(
    @SerialName("name")
    val name: String?,
    @SerialName("url")
    val url: String?
)
