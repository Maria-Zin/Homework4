package ru.fefu.countryexplorer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
    countryId: String,
    viewModel: CountryViewModel,
    navController: NavHostController
) {
    val country = viewModel.findCountryById(countryId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(country?.name ?: "Детали") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (country != null) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Название: ${country.name}", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Столица: ${country.capital ?: "-"}")
                    Text("Регион: ${country.region}")
                    Text("Население: ${country.population}")
                    Text("Площадь: ${country.area ?: "-"} кв. км.")

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.toggleFavourite(country) },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(if (viewModel.isFavourite(countryId)) "Удалить из избранного" else "Добавить в избранное")
                    }
                }
            } else {
                Text("Данные о стране не найдены", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}