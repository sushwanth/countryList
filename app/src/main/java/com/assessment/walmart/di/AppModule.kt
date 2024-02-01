package com.assessment.walmart.di

import com.assessment.walmart.data.datasource.remote.CountriesAPI
import com.assessment.walmart.data.repository.DataRepository
import com.assessment.walmart.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideCountryApiService(retrofit: Retrofit): CountriesAPI {
        return retrofit.create(CountriesAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        countriesAPI: CountriesAPI
    ): DataRepository {
        return DataRepository(countriesAPI)
    }
}