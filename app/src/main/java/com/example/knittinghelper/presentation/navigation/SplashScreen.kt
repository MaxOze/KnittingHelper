package com.example.knittinghelper.presentation.navigation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.R
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.auth.AuthenticationViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){
    val authViewModel : AuthenticationViewModel = hiltViewModel()
    val authValue = authViewModel.isUserAuthenticated
    val scale = remember{ Animatable(0f) }
    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(durationMillis = 1500, easing = {
                OvershootInterpolator(2f).getInterpolation(it)
            })
        )
        delay(1000)
        if(authValue){
            navController.navigate("projects_graph") {
                popUpTo(Screens.SplashScreen.route){
                    inclusive = true
                }
            }
        }
        else{
            navController.navigate(Screens.SignInScreen.route){
                popUpTo(Screens.SplashScreen.route){
                    inclusive = true
                }
            }
        }
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(id = R.drawable.ic_home_black_24dp),
            contentDescription = "Splash Screen Logo", modifier = Modifier.scale(scale.value))
    }
}