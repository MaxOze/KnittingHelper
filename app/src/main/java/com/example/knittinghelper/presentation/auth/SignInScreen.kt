package com.example.knittinghelper.presentation.auth


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.util.Response

@Composable
fun SignInScreen(navController: NavHostController){
    val viewModel : AuthenticationViewModel = hiltViewModel()

    val visible = remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier= Modifier.fillMaxSize().padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier= Modifier.weight(7f),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                val emailState = remember{
                    mutableStateOf("")
                }
                val passwordState = remember {
                    mutableStateOf("")
                }
//            Image(
//                painter = painterResource(id = R.drawable.ig_logo),
//                contentDescription = "LoginScreen Logo",
//                modifier= Modifier
//                    .width(250.dp)
//                    .padding(top = 16.dp)
//                    .padding(8.dp)
//            )
                Text(
                    text="Авторизация",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif
                )
                OutlinedTextField(value = emailState.value,
                    onValueChange ={
                        emailState.value = it
                    },
                    singleLine = true,
                    label = {
                        Text(text = "Email:")
                    }
                )
                OutlinedTextField(value = passwordState.value, onValueChange ={
                    passwordState.value = it
                },
                    label = {
                        Text(text = "Пароль:")
                    },
                    singleLine = true,
                    visualTransformation = if (visible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { visible.value = !visible.value }
                        ) {
                            Icon(
                                imageVector = if (visible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }
                )
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.signIn(
                                email = emailState.value,
                                password = passwordState.value
                            )
                        },
                        modifier = Modifier.padding(8.dp).weight(4f)
                    ) {
                        Text(text = "Войти")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(text="Новичок? Зарегистрируйся",
                    color= Color.Blue,
                    modifier= Modifier.padding(top = 10.dp)
                        .clickable {
                            navController.navigate(route = Screens.SignUpScreen.route) {
                                launchSingleTop = true
                            }
                        },
                    textDecoration = TextDecoration.Underline
                )
            }
        }
        Box(
            modifier= Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when(val response = viewModel.signInState.value) {
                is Response.Loading ->{
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Snackbar(modifier = Modifier.weight(5f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(text = "Авторизация...")
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                is Response.Success -> {
                    if(response.data) {
                        navController.navigate(Screens.ProjectsScreen.route) {
                            popUpTo(Screens.SignInScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
                is Response.Error -> {
                    Snackbar{
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = response.message)
                        }
                    }
                }
            }
        }
    }
}