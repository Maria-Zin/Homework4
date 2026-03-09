package ru.fefu.countryexplorer.data

import ru.fefu.countryexplorer.data.local.FavouriteDao
import ru.fefu.countryexplorer.data.local.FavouriteEntity
import javax.inject.Inject

class CountryRepository @Inject constructor(
    private val apiService: ApiService,
    private val favouriteDao: FavouriteDao
) {
    suspend fun getAllCountries(): Result<List<Country>> {
        return try {
            Result.success(apiService.getAllCountries())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchCountries(query: String): Result<List<Country>> {
        return try {
            Result.success(apiService.searchCountries(query))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavourites(): List<FavouriteEntity> = favouriteDao.getAllFavourites()

    suspend fun addFavourite(country: FavouriteEntity) = favouriteDao.insertFavourite(country)

    suspend fun removeFavourite(id: String) = favouriteDao.deleteFavourite(id)
}