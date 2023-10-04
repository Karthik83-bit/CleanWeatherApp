package com.example.weatherapp.domain.repo

import com.example.weatherapp.presentation.components.NetworkResponse
import com.example.weatherapp.data.model.CurrentWeatherResponseModel
import com.example.weatherapp.data.model.WeatherHourlyInfoResponse
import kotlinx.coroutines.flow.Flow

interface RepoImplementation {
    fun getWeatherInfo(): Flow<NetworkResponse<WeatherHourlyInfoResponse>>
    fun getCurrentWeather():Flow<NetworkResponse<CurrentWeatherResponseModel>>
}