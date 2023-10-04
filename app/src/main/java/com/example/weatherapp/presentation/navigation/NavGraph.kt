package com.example.weatherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.presentation.screens.StartScreen
import com.example.weatherapp.presentation.viewModel.StartScreenViewModel

@Composable

fun NavGraph(navController: NavHostController) {
    val navHostController= NavHost(navController =  navController, startDestination = "start"){
        composable("start"){
            StartScreen(navController, hiltViewModel<StartScreenViewModel>())
        }

    }
}