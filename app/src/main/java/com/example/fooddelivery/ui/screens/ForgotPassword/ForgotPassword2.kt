package com.example.fooddelivery.ui.screens.ForgotPassword


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults

import supabaseClient


@Composable
fun ForgotPassword2(navController: NavController, email: String) {
    var newPassword = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))

            TitleTexte("Reset Password")
            NormaleTexte("Please enter your new password.")
            Spacer(modifier = Modifier.height(30.dp))
            passwordTextField("New password", value = newPassword.value, onValueChange = { newPassword.value = it })
            passwordTextField("Confirm new password", value= confirmPassword.value, onValueChange= { confirmPassword.value= it })

            if (errorMessage.value.isNotEmpty()) {
                Text(errorMessage.value, color= Color.Red)
                Spacer(modifier= Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick= {
                if (newPassword.value != confirmPassword.value) {
                    errorMessage.value= "Passwords do not match"
                    return@Button
                }

                composableScope.launch(Dispatchers.IO) {
                    try {
                        // Call Supabase to update the password using the token from previous step
                        supabaseClient.auth.updateUser { this.password= newPassword.value.trim() }

                        // Navigate back or show success message
                        withContext(Dispatchers.Main) {
                            navController.navigate("SignUpSuccess") // or any other page after success.
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage.value= e.message ?: "Failed to reset password"
                        }
                    }
                }

            },modifier = Modifier
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
                    Text("Reset password",
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
fun ForgotPassword2 (navController: NavController){
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

            TitleTexte("Reset Password")
            Spacer(modifier = Modifier.height(5.dp))
            NormaleTexte("Please type something you’ll remember.")
            Spacer(modifier = Modifier.height(40.dp))

           // passwordTextField("Password")
            Spacer(modifier = Modifier.height(12.dp))
            //passwordTextField("Confirm password")
            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent("Continue",navController,"") // should go to the acceuil
        }}
}*/