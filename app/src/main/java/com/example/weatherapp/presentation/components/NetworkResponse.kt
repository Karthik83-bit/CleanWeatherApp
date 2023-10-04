package com.example.weatherapp.presentation.components

sealed class NetworkResponse<T>(val data:T?=null,val msg:String?=null){
    class isLoading<T>(): NetworkResponse<T>()
    class isSuccess<T>(data: T?): NetworkResponse<T>(data)
    class Failure<T>(msg:String): NetworkResponse<T>(msg=msg)
}
