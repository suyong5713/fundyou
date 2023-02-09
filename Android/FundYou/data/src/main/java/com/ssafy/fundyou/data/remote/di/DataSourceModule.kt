package com.ssafy.fundyou.data.remote.di


import com.ssafy.fundyou.data.remote.datasource.auth.AuthRemoteDataSource
import com.ssafy.fundyou.data.remote.datasource.auth.AuthRemoteDataSourceImpl
import com.ssafy.fundyou.data.remote.datasource.item.ItemRemoteDataSource
import com.ssafy.fundyou.data.remote.datasource.item.ItemRemoteDataSourceImpl
import com.ssafy.fundyou.data.remote.datasource.search.SearchRemoteDataSource
import com.ssafy.fundyou.data.remote.datasource.search.SearchRemoteDataSourceImpl
import com.ssafy.fundyou.data.remote.datasource.user.UserRemoteDataSource
import com.ssafy.fundyou.data.remote.datasource.user.UserRemoteDataSourceImpl
import com.ssafy.fundyou.data.remote.service.AuthApiService
import com.ssafy.fundyou.data.remote.service.ItemApiService
import com.ssafy.fundyou.data.remote.service.SearchApiService
import com.ssafy.fundyou.data.remote.service.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataSourceModule {
    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        authApiService: AuthApiService
    ): AuthRemoteDataSource = AuthRemoteDataSourceImpl(authApiService)

    @Provides
    @Singleton
    fun provideSearchRemoteDataSource(
        searchApiService: SearchApiService
    ): SearchRemoteDataSource = SearchRemoteDataSourceImpl(searchApiService)

    @Provides
    @Singleton
    fun provideItemRemoteDataSource(itemApiService: ItemApiService): ItemRemoteDataSource =
        ItemRemoteDataSourceImpl(itemApiService)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(UserApiService: UserApiService): UserRemoteDataSource =
        UserRemoteDataSourceImpl(UserApiService)
}