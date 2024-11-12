package com.tyris.data.di

import com.tyris.data.KtorCharacterRepository
import com.tyris.data.networking.HttpClientFactory
import com.tyris.domain.repositories.CharacterRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single{
        HttpClientFactory().build()
    }

    singleOf(::KtorCharacterRepository).bind<CharacterRepository>()

}