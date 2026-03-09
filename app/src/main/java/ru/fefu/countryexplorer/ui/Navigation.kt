package ru.fefu.countryexplorer.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: CountryViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            CountryListScreen(navController, viewModel)
        }
        composable(
            "detail/{countryId}",
            arguments = listOf(navArgument("countryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val countryId = backStackEntry.arguments?.getString("countryId") ?: ""
            CountryDetailScreen(countryId, viewModel, navController)
        }
        composable("favourites") {
            FavouritesScreen(navController, viewModel)
        }
    }
}