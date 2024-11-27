package com.yusuf.weaterapp.data.di

import com.yusuf.weaterapp.data.network.repository.LocalRepositoryImpl
import com.yusuf.weaterapp.domain.repository.LocalRepository
import com.yusuf.weaterapp.presentation.navigation.AppNavigator
import com.yusuf.weaterapp.presentation.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalModule {
    @Binds
    @Singleton
    fun bindLocalRepository(localRepository: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    fun bindNavigator(nav: AppNavigatorImpl): AppNavigator
}