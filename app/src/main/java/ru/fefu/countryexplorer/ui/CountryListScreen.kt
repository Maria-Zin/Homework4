package ru.fefu.countryexplorer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.fefu.countryexplorer.data.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
    navController: NavHostController,
    viewModel: CountryViewModel
) {
    val uiState by viewModel.uiState
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Страны мира") },
                actions = {
                    IconButton(onClick = { navController.navigate("favourites") }) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Избранное")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchCountries(it)
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Поиск стран...") }
            )

            when (val state = uiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Список пуст")
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.message, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadCountries() }) {
                                Text("Повторить")
                            }
                        }
                    }
                }
                is UiState.Success -> {
                    LazyColumn {
                        items(state.countries) { country ->
                            val countryId = viewModel.getCountryId(country.name)
                            CountryCard(
                                country = country,
                                isFavourite = viewModel.isFavourite(countryId),
                                onFavouriteClick = { viewModel.toggleFavourite(country) },
                                onClick = { navController.navigate("detail/$countryId") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountryCard(
    country: Country,
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = country.name, style = MaterialTheme.typography.titleLarge)
                Text(text = country.capital ?: "Нет столицы", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onFavouriteClick) {
                Text(if (isFavourite) "★" else "☆", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}