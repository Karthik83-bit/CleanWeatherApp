package com.example.weatherapp.data.repository

import com.example.weatherapp.presentation.components.NetworkResponse
import com.example.weatherapp.data.model.CurrentWeatherResponseModel
import com.example.weatherapp.data.model.WeatherHourlyInfoResponse
import com.example.weatherapp.data.src.ApiService
import com.example.weatherapp.domain.repo.RepoImplementation
import com.example.weatherapp.presentation.components.handleFlowResponse
import kotlinx.coroutines.flow.Flow

class Repository(val apiService: ApiService):RepoImplementation {
    override fun getWeatherInfo(): Flow<NetworkResponse<WeatherHourlyInfoResponse>> {
        return handleFlowResponse({ apiService.getForeCast() }, mapFun = {it})

    }

    override fun getCurrentWeather(): Flow<NetworkResponse<CurrentWeatherResponseModel>> {
        return handleFlowResponse({apiService.getCurrentWeather()},{it})
    }

}