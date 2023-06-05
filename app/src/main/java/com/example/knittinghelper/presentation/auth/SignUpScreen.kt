package com.example.knittinghelper.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(navController: NavHostController){
    val viewModel : AuthenticationViewModel = hiltViewModel()

    val visible = remember { mutableStateOf<Boolean>(false) }
    val nameError = remember{ mutableStateOf(false) }
    val emailError = remember{ mutableStateOf(false) }
    val passwordError = remember{ mutableStateOf(false) }

    Column(
        modifier= Modifier.fillMaxSize().padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    Box(
        modifier= Modifier.weight(7f),
        contentAlignment = Alignment.Center
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val userNameState = remember { mutableStateOf("") }
            val emailState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

//            Image(
//                painter = painterResource(id = R.drawable.ig_logo),
//                contentDescription = "LoginScreen Logo",
//                modifier= Modifier
//                    .width(250.dp)
//                    .padding(top = 16.dp)
//                    .padding(8.dp)
//            )
            Text(
                text = "Регистрация",
                modifier = Modifier.padding(10.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )
            OutlinedTextField(
                value = userNameState.value,
                onValueChange = { if (it.length <= 20) userNameState.value = it },
                singleLine = true,
                isError = nameError.value,
                supportingText = {
                    Row {
                        if (nameError.value) {
                            Icon(
                                imageVector = Icons.Outlined.Error,
                                contentDescription = "error",
                                modifier = Modifier.size(10.dp)
                            )
                        }
                        Text(text = "*обязательное поле")
                        Text(text = "${userNameState.value.length}/20")
                    }
                },
                label = {
                    Text(text = "Введите имя:")
                }
            )
            OutlinedTextField(value = emailState.value, onValueChange = {
                emailState.value = it
            },
                singleLine = true,
                isError = emailError.value,
                supportingText = {
                    if (emailError.value)
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Error,
                                contentDescription = "error",
                                modifier = Modifier.size(10.dp)
                            )
                            Text(text = "Введите Email")
                        }
                },
                label = {
                    Text(text = "Введите Email:")
                }
            )
            OutlinedTextField(value = passwordState.value, onValueChange = {
                passwordState.value = it
            },
                singleLine = true,
                isError = passwordError.value,
                label = {
                    Text(text = "Введите пароль:")
                },
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
                },
                supportingText = {
                    if (passwordError.value)
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Error,
                                contentDescription = "error",
                                modifier = Modifier.size(10.dp)
                            )
                            Text(text = "Пароль должен быть больше 6 символов")
                        }
                }
            )
            Button(
                onClick = {
                    nameError.value = userNameState.value.isEmpty()
                    passwordError.value = passwordState.value.length <= 6

                    val isValid = emailState.value.matches(
                        Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
                    )
                    emailError.value = !isValid

                    if (!emailError.value && !nameError.value && !passwordError.value)
                        viewModel.signUp(
                            email = emailState.value,
                            password = passwordState.value,
                            username = userNameState.value
                    )
                },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            ) {
                Text(text = "Зарегистрироваться")
            }
            Text(
                text = "Уже зарегистрирован? Заходи", modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(route = Screens.SignInScreen.route) {
                            launchSingleTop = true
                        }
                    },
                textDecoration = TextDecoration.Underline
            )
        }
    }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val response = viewModel.signUpState.value) {
                is Response.Loading -> {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Snackbar(modifier = Modifier.weight(5f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(text = "Регистрация...")
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
                    if (response.data) {
                        Snackbar {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Text(text = "Регистрация...")
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        LaunchedEffect(key1 = true) {
                            delay(1000)
                            navController.navigate("projects_graph") {
                                popUpTo(Screens.SplashScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
                is Response.Error -> {
                    Snackbar(modifier = Modifier.padding(16.dp)) {
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
