package org.toch.rickmortytest.presentation.utils.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import rickandmortytest.shared.generated.resources.Res
import rickandmortytest.shared.generated.resources.search_bar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp), // Ajustamos el padding para el LazyColumn
        expanded = false,
        onExpandedChange = { },
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
                placeholder = { Text(stringResource(Res.string.search_bar)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                }
            )
        }
    ) {}
}