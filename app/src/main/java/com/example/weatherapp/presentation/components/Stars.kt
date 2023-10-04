package com.example.weatherapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun Stars() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.5f))
    {
        for(i in 1..1000){
            val mathrandom=Math.random()
            Box(modifier = Modifier
                .size(Random.nextDouble(0.0, 1.5).dp)
                .offset(Random.nextInt(0, 500).dp, Random.nextInt(0, 500).dp)
                .background(Color.White))
        }
    }

}