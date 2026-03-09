package ru.fefu.countryexplorer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    navController: NavHostController,
    viewModel: CountryViewModel
) {
    val favourites by viewModel.favouritesList

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранные страны") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (favourites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Нет избранных стран", style = MaterialTheme.typography.titleMedium)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(favourites) { favCountry ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { navController.navigate("detail/${favCountry.id}") }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(favCountry.name, style = MaterialTheme.typography.titleMedium)
                                Text("Столица: ${favCountry.capital ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { viewModel.removeFavouriteById(favCountry.id) }) {
                                Text("★", style = MaterialTheme.typography.headlineMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}