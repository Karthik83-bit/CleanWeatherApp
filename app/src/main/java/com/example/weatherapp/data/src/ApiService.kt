package com.example.weatherapp.data.src

import com.example.weatherapp.data.model.CurrentWeatherResponseModel
import com.example.weatherapp.data.model.WeatherHourlyInfoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("forecast?lat=20.2961&lon=85.8245&appid=65db79dee604213306c95f14d6a588a7")
    suspend fun getForeCast():Response<WeatherHourlyInfoResponse>

    @GET("weather?lat=20.2961&lon=85.8245&appid=65db79dee604213306c95f14d6a588a7")
    suspend fun getCurrentWeather():Response<CurrentWeatherResponseModel>
}