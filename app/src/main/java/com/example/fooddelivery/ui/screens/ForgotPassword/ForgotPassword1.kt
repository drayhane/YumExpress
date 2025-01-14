package com.example.fooddelivery.ui.screens.ForgotPassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.TitleTexte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supabaseClient


@Composable
fun ForgotPassword1(navController: NavController) {
    var email = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))
            TitleTexte("Forgot Password?")
            Spacer(modifier = Modifier.height(5.dp))
            NormaleTexte("Please enter your email address.")
            Spacer(modifier = Modifier.height(30.dp))

            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })

            if (errorMessage.value.isNotEmpty()) {
                Text(errorMessage.value, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(onClick = {
                if (email.value.isBlank()) {
                    errorMessage.value = "Email field is required"
                    return@Button
                }

                // Call Supabase to send OTP
                composableScope.launch(Dispatchers.IO) {
                    try {
                        supabaseClient.auth.resetPasswordForEmail(email.value.trim())
                        withContext(Dispatchers.Main) {
                            navController.navigate("ForgotPasswordOTP?email=${email.value}")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Failed to send OTP"
                        }
                    }
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(56.dp),

                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Black),
                shape = RoundedCornerShape(8.dp)

            ) {
                Box (modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp)
                    .background(
                        color = Color.Black,
                    ),
                    contentAlignment = Alignment.Center)
                {
                    Text("Next",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

        }
    }
}



/*
@Composable
fun ForgotPassword1 (navController: NavController){

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()// to vocer the whole screen

    ){

        Column (

            modifier = Modifier
                .fillMaxSize() // Makes the Column fill the entire screen
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            //verticalArrangement = Arrangement.Center // Center vertically
        ) {
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Forgot password ? ")
            Spacer(modifier = Modifier.height(5.dp))
            NormaleTexte("Don’t worry! It happens. Please enter the email associated with your account.")
            Spacer(modifier = Modifier.height(30.dp))

            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
            Spacer(modifier = Modifier.height(12.dp))
            passwordTextField("Confirm password", value = confirmPassword.value, onValueChange = { confirmPassword.value = it })


            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent("Next",navController,"ForgotPasswordOTP") // should go to the acceuil
        }}
}*/