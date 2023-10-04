package com.example.weatherapp.presentation.screens

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScopeInstance.weight
//import androidx.compose.foundation.layout.RowScopeInstance.weight
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.weatherapp.R
import com.example.weatherapp.presentation.components.Moon
import com.example.weatherapp.presentation.components.Stars
import com.example.weatherapp.presentation.components.Sun
import com.example.weatherapp.presentation.viewModel.StartScreenViewModel
import com.example.weatherapp.ui.theme.evenin
import com.example.weatherapp.ui.theme.evenin_dark
import com.example.weatherapp.ui.theme.evenin_m
import com.example.weatherapp.ui.theme.night
import com.example.weatherapp.ui.theme.night_dark
import com.example.weatherapp.ui.theme.sky
import com.example.weatherapp.ui.theme.sky_drak
import com.example.weatherapp.ui.theme.sunny
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.util.Calendar
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTextApi::class)
@Composable
fun StartScreen(navController: NavHostController, hiltViewModel: StartScreenViewModel) {


    val nightGradient=Brush.linearGradient(listOf(night_dark, night))
    var space:Float=0f
    var vspace:Float=0f
    var convertsionParameter:Float=0f

    val sunnyGradient=Brush.linearGradient(listOf(sky_drak, sky))
    val eveningGradient=Brush.linearGradient(listOf(evenin_dark, evenin))
    val isEvenin= androidx.compose.runtime.remember{
        mutableStateOf(false)
    }
    val isNight= remember{
        mutableStateOf(false)
    }
    val isDay= remember{
        mutableStateOf(true)
    }
    val star= remember {
        mutableStateOf(false)

    }
    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    val paint = Paint()
    paint.textSize = 30f
    paint.typeface = Typeface.DEFAULT_BOLD
    paint.color = Color.White.toArgb()
    val yoff= remember {
        mutableStateOf(10f)
    }
    val xoff= remember {
        mutableStateOf(10f)
    }

    val morningTransition= remember{mutableStateOf(false)}
    val eveningTransition= remember{mutableStateOf(false)}
    val nightTransition= remember{mutableStateOf(false)}
    val color= remember{mutableStateOf(sky)}
    val morning_to_eve=animateColorAsState(targetValue = if(morningTransition.value) evenin_dark else sky , animationSpec = tween(360*100))
    val eve_to_night=animateColorAsState(targetValue = if(eveningTransition.value) night_dark else evenin_dark , animationSpec = tween(360*100))
    val night_to_morning=animateColorAsState(targetValue = if(nightTransition.value) sunny else night_dark , animationSpec = tween(360*100))
    val forparam by remember {
        mutableStateOf( Math.abs(10-LocalTime.now().hour)*(360/10))
    }
    var timeDelay  by remember {
        mutableStateOf( Math.abs(10-hiltViewModel.presentHour.value)/(360-Math.abs(10-hiltViewModel.presentHour.value)*(360/10))*3600*1000)
    }
    val drageStartAngle= remember{
        mutableStateOf(0f)
    }
    val touchStateAngle= remember {
        mutableStateOf(0f)
    }
    val oldPositionvalue=remember{ mutableStateOf(0f) }

    LaunchedEffect(key1 = isDay.value,isEvenin.value,isNight.value){


        hiltViewModel.getCurrentWeather()
        hiltViewModel.getWeatherData() {

            Log.d("kres1", "StartScreen:${hiltViewModel.resp} ")

        }
        timeDelay=
            Math.abs(10-hiltViewModel.presentHour.value)/(360-Math.abs(10-hiltViewModel.presentHour.value)*(360/10))*3600*1000



//            isEvenin.value->Math.abs(hiltViewModel.presentHour.value-3)*(360/3)

//            isNight.value->Math.abs(11-hiltViewModel.presentHour.value)*(360/11)
//            else -> {0}
//        }


        for(i in forparam..360){
            Log.d("killker", "StartScreen:${timeDelay.toString() +" "+forparam} ")

                delay(timeDelay.toLong())


            if(i%180<180 && i>180){
                yoff.value=(180-(i-180)).toFloat()
            }
            else{
                if(i%180==0) yoff.value=180.toFloat() else{
                    yoff.value=(i%180).toFloat()}
            }
            xoff.value=i.toFloat()

        }



    }
    LaunchedEffect(key1 = true){

        while (true) {

            val presentTime = LocalTime.now()
            hiltViewModel.presentHour.value=presentTime.hour

            val morningStartTime = LocalTime.of(6, 0)
            val eveningStartTime = LocalTime.of(16, 0)
            val nightStartTime=LocalTime.of(19,0)
            val presentHour=presentTime.hour



            when (true) {
                presentTime.isBefore(eveningStartTime) -> {

                    isNight.value = false
                    isDay.value = true
                    isEvenin.value = false
                    morningTransition.value=true
                    color.value=morning_to_eve.value
                }
                presentTime.isAfter(nightStartTime)-> {
                    isNight.value = true
                    isDay.value = false
                    isEvenin.value = false
                    nightTransition.value=true
                    color.value=night_to_morning.value
                }

                presentTime.isAfter(eveningStartTime) -> {


//                    isNight.value=false
//                    isDay.value=false
//                    isEvenin.value=true
                    isNight.value = false
                    isDay.value = false
                    isEvenin.value = true
                    eveningTransition.value=true
                    color.value=eve_to_night.value


                }



                else -> {}
            }

delay(1000)
        }

    }





    if(hiltViewModel.isError.value){
        AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { Button(onClick = { hiltViewModel.isError.value=false }) {
            Text("Ok")
        }}, title = {Text("Error")}, text = {Text(hiltViewModel.errorMag.value)})
    }
    if(hiltViewModel.isLoading.value){
        Dialog(onDismissRequest = { /*TODO*/ }) {
           CircularProgressIndicator()
        }
    }

    val width = animateDpAsState(targetValue = if(isNight.value)0.dp else 900.dp, tween(1000))
//    brush = if (isDay.value) sunnyGradient else if (isEvenin.value) eveningGradient else nightGradient)
    Box(modifier= Modifier
        .fillMaxSize()
        .background(brush = if(isDay.value)sunnyGradient else if(isEvenin.value) eveningGradient else
            nightGradient)
        .pointerInput(true) {
            detectDragGestures(onDragStart = { offset ->


            }, onDrag = { _, _ -> }, onDragEnd = {})
        }){
        if(isNight.value){
            Stars()
        }
        if(isNight.value){
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
            Box(
                Modifier
                    .size(150.dp)
                    .offset(y =  20.dp, x = (  30f).dp)
                    .background(Color.White, CircleShape))

            Box(modifier = Modifier
                .rotate(50f)
                .offset(y = 400.dp)){
                Box(
                    Modifier
                        .width(width.value)
                        .height(200.dp)
                        .graphicsLayer {
                            rotationX = 40f
                            rotationY = 89f
                            translationX = 190f
                            translationY = 80f
                        }
                        .background(
                            brush = Brush.linearGradient(
                                listOf(night.copy(0.5f), Color.White),
                                start = Offset(x = 10f, 200f),
                                end = Offset(1000f, 2900f)
                            )
                        ))
            }}
        }
        if(isDay.value){
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                .fillMaxWidth()
                .offset(x = xoff.value.dp, y = -yoff.value.dp)) {
                Spacer(modifier =Modifier.height(200.dp))
                Sun()
            }

        }
        if(isEvenin.value){
            Box(){
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.offset(30f.dp,30f.dp)) {

                Box(){
                Stars()

                Moon()}

            }}

        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text("${((hiltViewModel.temp.value- 273.15)).roundToInt()}\u2103", fontSize = 50.sp, color = Color.White, fontFamily = FontFamily(fonts = listOf(
                Font(R.font.header)
            )))
            Text("${hiltViewModel.weatherTextDesc.value.capitalize()}", fontSize = 30.sp, color = Color.White,)
//
//            Box(modifier = Modifier
//                .size(100.dp)
//                .background(shape = CircleShape, color = sunny))
//            if(isDay.value){
//                Sun()
//            }
//            else{
//                Moon()
//            }



//            Box(Modifier.size(300.dp)) {
//                Box(modifier = Modifier
//                    .size(100.dp)
//                    .background(shape = CircleShape, color = Color.White))
//                Box(modifier = Modifier
//                    .size(100.dp)
//                    .graphicsLayer {
//                        translationX = -25f
//                        translationY = -50f
//                    }
//                    .background(shape = CircleShape, color = evenin))
//
//            }
            Box(modifier = Modifier
                .weight(1f)
               ) {
                Column() {
                    Spacer(modifier = Modifier.weight(0.8f))

                    Image(painter=painterResource(id=R.drawable.mountain),""
                        ,modifier = Modifier
                            .fillMaxWidth()
                            .scale(2.2f),  contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(
                            if(isDay.value) sunny else Color.White, BlendMode.Dst))


                }}
            MyGraph()
            Box(modifier = Modifier

                .drawBehind {

                    space = size.width / 40
                    vspace = (size.height / 4).toFloat()
                    convertsionParameter = size.height / 40


                    for (i in 0..3) {
                        drawContext.canvas.nativeCanvas.drawText(
                            "${10 * (4 - i)}", 30f, (i * vspace), paint
                        )
                    }
                }){


            Row(modifier = Modifier
                .horizontalScroll(state = hiltViewModel.scrollState.value, enabled = true)
                .drawBehind {

                    space = size.width / 40
                    vspace = (size.height / 4).toFloat()
                    convertsionParameter = size.height / 40

                    for (i in 1 until 40) {
//if(hiltViewModel.resp?.size !=0){
//    rotate(-100f, pivot = Offset( (i * space), size.height)){
//                        drawContext.canvas.nativeCanvas.drawText(
//                            "${hiltViewModel.resp?.get(i)?.dtTxt?.split(" ")?.get(0)}", (i * space), size.height - 10f, paint
//                        )}}else{

                        drawContext.canvas.nativeCanvas.drawText(
                            "${(3 * i) % 24}", (i * space), size.height - 10f, paint
                        )
                    }


                })
            {



                        Box(
                            Modifier
                                .height(200.dp)
                                .width(2000.dp)

                        ){
                            Canvas(
                                modifier = Modifier.fillMaxSize(),

                                ) {

//                            var space=size.width/7
//                            val vspace=size.height/4
//                            val paint=Paint()
//                            paint.textSize=30f
//                            paint.color= Color.White.toArgb()
//                            for(i in 1..7){
//
//
//                                drawContext.canvas.nativeCanvas.drawText("${100*(i)}",(i*space),size.height-10f,paint
//                                )
//                            }
//                            for(i in 0..4){
//                                drawContext.canvas.nativeCanvas.drawText("${100*(i)}",30f,(i*vspace),paint
//                                )
//                            }

                                // Create a Pat
                                // h object
                                val path = Path()


                                // Move to the starting point


                                // Add lines and curves to the path

                                for (j in 0 until 40) {
                                    Log.d("karth" , "StartScreen: ${(j) }")

                                    if (hiltViewModel.resp?.size  != 0) {
                                        Log.d("karthi" , "StartScreen: ${(hiltViewModel.resp?.get(j)?.main) }")
                                       (hiltViewModel.resp?.get(j)?.main?.temp?.minus(273.15))?.times(
                                            convertsionParameter
                                        )?.let { it1 ->
                                           Log.d("kart" , "StartScreen: ${(j) }")

                                           drawCircle(Color.White, radius = 5f, center = Offset((j) *space,
                                               -it1.toFloat()+700f))

                                           drawContext.canvas.nativeCanvas.drawText("${
                                               hiltViewModel.resp?.get(j)?.main?.temp?.minus(273.15)!!
                                                   .roundToInt()}",((j) *space), -it1.toFloat()+700f,paint)
                                           rotate(-90f, pivot =  Offset((j) *space,
                                               -it1.toFloat()+700f)){
                                           drawContext.canvas.nativeCanvas.drawText("${
                                               hiltViewModel.resp?.get(j)?.weather?.get(0)?.description
                                           }",((j) *(space+06f)), it1.toFloat()-35f,paint)}
//
                                           if(j==0){
                                               path.moveTo((j) *space,
                                                   -it1.toFloat()+700f)
                                           }
                                           else{
                                               path.lineTo(
                                                   (j) *space,
                                                   -it1.toFloat()+700f
                                               )
                                           }


                                        }
                                    }

                                }
                                drawPath(
                                    path = path,
                                    color = Color.White,
                                    style = Stroke(width = 2.dp.toPx())
                                )


//                    path.quadraticBezierTo(300f, 94f, 100f, 0f)
//                    path.quadraticBezierTo(600f, 150f, 900f, 250f)


//                    path.cubicTo(550f, 300f, 800f, 100f, 450f, 50f)

                                // Draw the path on the canvas

                            }










                }


            }
            }

            Button(onClick = { hiltViewModel.getWeatherData {  } }) {
               Text("kj")
            }



        }
      
    }

}

@Composable
fun MyGraph() {

}
