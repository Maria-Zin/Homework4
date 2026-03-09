package ru.fefu.countryexplorer.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.fefu.countryexplorer.data.Country
import ru.fefu.countryexplorer.data.CountryRepository
import ru.fefu.countryexplorer.data.local.FavouriteEntity
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val countries: List<Country>) : UiState()
    data class Error(val message: String) : UiState()
    object Empty : UiState()
}

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    private var searchJob: Job? = null

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _favouritesList = mutableStateOf<List<FavouriteEntity>>(emptyList())
    val favouritesList: State<List<FavouriteEntity>> = _favouritesList

    private val _countriesCache = mutableStateOf<Map<String, Country>>(emptyMap())

    init {
        loadFavouritesFromDb()
        loadCountries()
    }

    private fun loadFavouritesFromDb() {
        viewModelScope.launch {
            _favouritesList.value = repository.getFavourites()
        }
    }

    fun loadCountries() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val result = repository.getAllCountries()
                if (result.isSuccess) {
                    val countries = result.getOrDefault(emptyList())
                    updateCache(countries)
                    _uiState.value = if (countries.isEmpty()) UiState.Empty else UiState.Success(countries)
                } else {
                    _uiState.value = UiState.Error("Не удалось загрузить страны")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ошибка: Проверьте интернет")
            }
        }
    }

    fun searchCountries(query: String) {
        if (query.isBlank()) {
            loadCountries()
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                delay(500)
                _uiState.value = UiState.Loading
                val result = repository.searchCountries(query)
                if (result.isSuccess) {
                    val countries = result.getOrDefault(emptyList())
                    updateCache(countries)
                    _uiState.value = if (countries.isEmpty()) UiState.Empty else UiState.Success(countries)
                } else {
                    _uiState.value = UiState.Error("Ничего не найдено")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ошибка поиска")
            }
        }
    }

    private fun updateCache(countries: List<Country>) {
        val newCache = _countriesCache.value.toMutableMap()
        countries.forEach { newCache[it.name.urlEncode()] = it }
        _countriesCache.value = newCache
    }

    fun findCountryById(countryId: String): Country? = _countriesCache.value[countryId]

    fun isFavourite(countryId: String): Boolean {
        return _favouritesList.value.any { it.id == countryId }
    }

    fun toggleFavourite(country: Country) {
        val id = country.name.urlEncode()
        viewModelScope.launch {
            if (isFavourite(id)) {
                repository.removeFavourite(id)
            } else {
                val entity = FavouriteEntity(
                    id = id,
                    name = country.name,
                    capital = country.capital,
                    region = country.region,
                    population = country.population,
                    area = country.area
                )
                repository.addFavourite(entity)
            }
            loadFavouritesFromDb()
        }
    }

    fun removeFavouriteById(id: String) {
        viewModelScope.launch {
            repository.removeFavourite(id)
            loadFavouritesFromDb()
        }
    }

    fun getCountryId(countryName: String): String = countryName.urlEncode()
}

fun String.urlEncode(): String = this.replace(" ", "_")