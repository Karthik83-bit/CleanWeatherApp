package com.example.weatherapp.di.servicemodule

import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.src.ApiService
import com.example.weatherapp.domain.repo.RepoImplementation
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitProvider {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(" https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create(Gson())).build()

    }
@Provides
@Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService): RepoImplementation {
        return Repository(apiService)
    }
}