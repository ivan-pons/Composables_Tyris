package com.tyris.pagingLazy.characters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.tyris.domain.model.CharacterBO
import com.tyris.pagingLazy.composables.CharacterGrid
import com.tyris.pagingLazy.composables.CustomTopAppBar
import com.tyris.pagingLazy.composables.ListCharacters
import com.tyris.pagingLazy.ui.theme.ComposablesTyrisTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersScreenRoot(
    onCharacterClicked: (CharacterBO) -> Unit,
    viewModel: CharactersViewModel = koinViewModel()
) {
    CharactersScreen(
        state = viewModel.charactersState,
        onAction = {
            when (it) {
                is CharactersActions.CharacterClicked -> onCharacterClicked(it.character)
            }
            viewModel.onAction(it)
        }
    )
}

@Composable
private fun CharactersScreen(
    state: StateFlow<PagingData<CharacterBO>>,
    onAction: (CharactersActions) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Personajes")
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
        ) {
            ListCharacters(
                characters = state,
                onCharacterClicked = {
                    onAction(CharactersActions.CharacterClicked(it))
                }
            )
        }
    }
}

@Preview
@Composable
fun CharactersScreenPreview() {
    ComposablesTyrisTheme {
        CharactersScreen(
            state = MutableStateFlow(PagingData.empty()),
            onAction = {}
        )
    }
}