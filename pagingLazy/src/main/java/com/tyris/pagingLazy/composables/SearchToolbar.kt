package com.tyris.pagingLazy.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyris.pagingLazy.ui.theme.ComposablesTyrisTheme

@Composable
fun SearchToolbar(
    query: String,
    hint: String,
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            value = query,
            onValueChange = { onQueryChanged(it) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = {
               Text(text = hint)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { onSearchClicked() },
            modifier = Modifier
                .size(40.dp)
            ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun SearchToolbarPreview(){
    ComposablesTyrisTheme {
        SearchToolbar(
            query = "",
            hint = "buscar",
            onQueryChanged = {},
            onSearchClicked = {}
        )
    }
}