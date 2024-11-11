package com.tyris.data.mappers

import com.tyris.data.networking.model.CharacterDto
import com.tyris.data.networking.model.ShortLocationDto
import com.tyris.data.utils.getUrlIds
import com.tyris.domain.model.CharacterBO
import com.tyris.domain.model.ShortLocationBO


fun CharacterDto.toCharacterBO(): CharacterBO =
    CharacterBO(
        created = this.created ?: "",
        gender = this.gender ?: "",
        id = this.id ?: -1,
        image = this.image ?: "",
        name = this.name ?: "",
        species = this.species ?: "",
        status = this.status ?: "",
        url = this.url ?: "",
        type = this.type ?: "",
        location = this.location.toLocationBO(),
        origin = this.origin.toLocationBO(),
        episodesId = getUrlIds(this.episode),
    )

fun ShortLocationDto?.toLocationBO(): ShortLocationBO =
    ShortLocationBO(
        name = this?.name ?: "",
        id = this?.url?.substringAfterLast("/") ?: ""
    )

