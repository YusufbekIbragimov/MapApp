package com.yusuf.weaterapp.data.di

import android.content.Context
import androidx.room.Room
import com.yusuf.weaterapp.data.room.AppDatabase
import com.yusuf.weaterapp.data.room.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase = Room.databaseBuilder(
        appContext, AppDatabase::class.java, "app_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideCardDao(database: AppDatabase): WeatherDao {
        return database.cardDao()
    }
}