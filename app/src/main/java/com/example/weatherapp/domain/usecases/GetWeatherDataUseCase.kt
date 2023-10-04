package com.example.weatherapp.domain.usecases

import com.example.weatherapp.presentation.components.NetworkResponse
import com.example.weatherapp.data.model.WeatherHourlyInfoResponse
import com.example.weatherapp.domain.repo.RepoImplementation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(val repoImplementation: RepoImplementation) {
    fun invoke(): Flow<NetworkResponse<WeatherHourlyInfoResponse>> {
        return repoImplementation.getWeatherInfo()
    }
}