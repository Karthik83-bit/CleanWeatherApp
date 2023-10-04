package com.example.weatherapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.evenin

@Composable
fun Moon() {
    Box(Modifier.size(300.dp).drawBehind {
        drawCircle(Color.White, center = this.center, radius = 1f)
    }) {
//        Box(modifier = Modifier
//            .size(100.dp)
//            .background(shape = CircleShape, color = Color.White))
        Box(modifier = Modifier.drawBehind {

            drawCircle(Color.White, center = this.center, radius = 1f)
        }
            .size(100.dp)
            .graphicsLayer {
                translationX=-25f
                translationY=-50f
            }
            .background(shape = CircleShape, color = Color.White))

    }
}