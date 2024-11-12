package com.tyris.pagingLazy.di

import com.tyris.pagingLazy.characters.CharactersViewModel
import com.tyris.pagingLazy.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::CharactersViewModel)
    viewModelOf(::SearchViewModel)

}