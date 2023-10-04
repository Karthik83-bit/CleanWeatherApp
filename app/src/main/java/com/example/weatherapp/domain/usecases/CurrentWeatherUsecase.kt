package com.example.weatherapp.domain.usecases

import com.example.weatherapp.presentation.components.NetworkResponse
import com.example.weatherapp.data.model.CurrentWeatherResponseModel
import com.example.weatherapp.domain.repo.RepoImplementation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrentWeatherUsecase @Inject constructor(val repository: RepoImplementation) {
    fun invoke(): Flow<NetworkResponse<CurrentWeatherResponseModel>> {
        return repository.getCurrentWeather()
    }
}