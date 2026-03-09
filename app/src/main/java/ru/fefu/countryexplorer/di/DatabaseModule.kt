package ru.fefu.countryexplorer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.fefu.countryexplorer.data.local.AppDatabase
import ru.fefu.countryexplorer.data.local.FavouriteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "country_database"
        ).build()
    }

    @Provides
    fun provideFavouriteDao(database: AppDatabase): FavouriteDao {
        return database.favouriteDao()
    }
}