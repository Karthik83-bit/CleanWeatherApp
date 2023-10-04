package com.example.weatherapp.presentation.viewModel

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.ListItem
import com.example.weatherapp.data.model.WeatherHourlyInfoResponse
import com.example.weatherapp.domain.usecases.CurrentWeatherUsecase
import com.example.weatherapp.domain.usecases.GetWeatherDataUseCase
import com.example.weatherapp.presentation.components.handleFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(val getWeatherDataUseCase: GetWeatherDataUseCase,val currentWeatherUsecase: CurrentWeatherUsecase):ViewModel() {
    val scrollState= mutableStateOf(ScrollState(0))
    val presentHour= mutableStateOf(1)
    val isLoading= mutableStateOf(false)
    val isError= mutableStateOf(false)
    val errorMag= mutableStateOf("")
    val temp= mutableStateOf(0.0)
    val state=mutableStateOf("")
        val weatherTextDesc=mutableStateOf("")
    var resp:List<ListItem?>? = mutableStateListOf()

    fun getWeatherData(onSuccess:()->Unit) {
        handleFlow(response = getWeatherDataUseCase.invoke(), loading = isLoading, error = isError, errormsg = errorMag){
            Log.d("kres", "getWeatherData: ${it}")





            resp= it.list
            onSuccess()


        }
    }
    fun getCurrentWeather(){
        handleFlow(currentWeatherUsecase.invoke(), loading = isLoading, errormsg = errorMag, error = isError){
            temp.value= it.main.temp!!
            weatherTextDesc.value=it.weather[0].description


        }
    }
}