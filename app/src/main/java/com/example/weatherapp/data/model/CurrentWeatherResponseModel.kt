package com.example.weatherapp.data.model

data class CurrentWeatherResponseModel(
    val base: String,
    val clouds: CurrClouds,
    val cod: Int,
    val coord: CurrCoord,
    val dt: Int,
    val id: Int,
    val main: CurrMain,
    val name: String,
    val sys: CurrSys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<CurrWeather>,
    val wind: CurrWind
)