package com.example.fooddelivery.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fooddelivery.R
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.ButtonComponent
import com.example.fooddelivery.components.GoogleLoginIn
import com.example.fooddelivery.components.DontHaveAccount
import com.example.fooddelivery.components.HaveAccount
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.OrSeparator
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField
import com.example.fooddelivery.data.model.UserViewModel
import com.example.fooddelivery.ui.theme.MainBlack
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.OTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supabaseClient


@Composable
fun SignUp2 (navController: NavController){

    // to acces the infos whenver i'm
    val userViewModel: UserViewModel = viewModel()


    // State variables for input fields
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
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
        ){
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Sign Up")
            Spacer(modifier = Modifier.height(20.dp))

            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
            Spacer(modifier = Modifier.height(12.dp))
            passwordTextField("Confirm password", value = confirmPassword.value, onValueChange = { confirmPassword.value = it })


            Spacer(modifier = Modifier.weight(1f))

            // Display error message if any
            if (errorMessage.value.isNotEmpty()) {
                Text(errorMessage.value, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }


            // Button to handle sign-up
            Button(onClick = {
                userViewModel.email.value = email.value
                userViewModel.password.value = password.value


                // Handle sign-up logic
                if (password.value != confirmPassword.value) {
                    errorMessage.value = "Passwords do not match"
                    return@Button
                }

                if (email.value.isBlank() || password.value.isBlank() ) {
                    errorMessage.value = "All fields are required"
                    return@Button
                }

                // Call Supabase to sign up
                composableScope.launch(Dispatchers.IO) {
                    try {
                        //val result = supabaseClient.auth.signUpWith(Google)
                        val result = supabaseClient.auth.signUpWith(Email) {
                            this.email= email.value  // Pass the value, not the state
                            this.password = password.value  // Pass the value, not the state
                        }

                        // Switch to Main thread for navigation
                        withContext(Dispatchers.Main) {
                            navController.navigate("SignUp3OTP?email=${email.value}")
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Sign-up failed"
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
                        color = MainBlack,
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




            OrSeparator()

            GoogleLoginIn{}


            Spacer(modifier = Modifier.height(15.dp))
            HaveAccount(navController)


        }
    }
}

/* This worked
@Composable
fun SignUp2 (navController: NavController){

    // State variables for input fields
    var name = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
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
        ){
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Sign Up")
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField("Name", value = name.value, onValueChange = { name.value = it })
            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
            Spacer(modifier = Modifier.height(12.dp))
            passwordTextField("Confirm password", value = confirmPassword.value, onValueChange = { confirmPassword.value = it })


            Spacer(modifier = Modifier.weight(1f))

            // Display error message if any
            if (errorMessage.value.isNotEmpty()) {
                Text(errorMessage.value, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }


            // Button to handle sign-up
            Button(onClick = {
                // Handle sign-up logic
                if (password.value != confirmPassword.value) {
                    errorMessage.value = "Passwords do not match"
                    return@Button
                }

                if (email.value.isBlank() || password.value.isBlank() || name.value.isBlank()) {
                    errorMessage.value = "All fields are required"
                    return@Button
                }

                // Call Supabase to sign up
                composableScope.launch(Dispatchers.IO) {
                    try {
                        //val result = supabaseClient.auth.signUpWith(Google)
                        val result = supabaseClient.auth.signUpWith(Email) {
                            this.email= email.value  // Pass the value, not the state
                            this.password = password.value  // Pass the value, not the state
                        }



                        // Switch to Main thread for navigation
                        withContext(Dispatchers.Main) {
                            navController.navigate("SignUp3OTP")
                        }

                        // Navigate to OTP page
                        navController.navigate("SignUp3OTP")
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Sign-up failed"
                        }
                    }
                }
            }) {
                Text("Next")
            }


            ButtonComponent("Next",navController,"SignUp3OTP")

            OrSeparator()

            GoogleLoginIn{}


            Spacer(modifier = Modifier.height(15.dp))
            HaveAccount(navController)


        }
    }
}/*


/*
fun SignUp2 (navController: NavController){
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
        ){
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Sign Up")
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField("Name")
            MyTextField("Email address")
            passwordTextField("Password")
            Spacer(modifier = Modifier.height(12.dp))
            passwordTextField("Confirm password")


            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent("Next",navController,"SignUp3OTP")

            OrSeparator()

            GoogleLoginIn{}


            Spacer(modifier = Modifier.height(15.dp))
            HaveAccount(navController)


        }
    }
}*/