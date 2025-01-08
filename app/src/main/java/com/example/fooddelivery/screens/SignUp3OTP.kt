package com.example.fooddelivery.screens

import java.io.IOException

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
import com.example.fooddelivery.R
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.ButtonComponent
import com.example.fooddelivery.components.GoogleLoginIn
import com.example.fooddelivery.components.DontHaveAccount
import com.example.fooddelivery.components.HaveAccount
import com.example.fooddelivery.components.LinkText
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.OrSeparator
import com.example.fooddelivery.components.OtpTextField
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supabaseClient


@Composable
fun SignUp3OTP (navController: NavController, email: String) {


    var otp = remember { mutableStateOf(MutableList(6) { "" }) }
    val errorMessage = remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()



    fun resendOtp() {

        composableScope.launch(Dispatchers.IO) {
            try {
                //val result = supabaseClient.auth.signUpWith(Google)
                val result = supabaseClient.auth.signUpWith(Email) {
                    this.email= email  // Pass the value, not the state
                }

                // Switch to Main thread for navigation
                withContext(Dispatchers.Main) {
                    navController.navigate("SignUp3OTP?email=${email}")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage.value = e.message ?: "Sign-up failed"
                }
            }
        }

    }



    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()// to vocer the whole screen

    ) {

        Column(

            modifier = Modifier
                .fillMaxSize() // Makes the Column fill the entire screen
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            verticalArrangement = Arrangement.Center, // Center vertically
            //verticalArrangement = Arrangement.Center // Center vertically
        ) {
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Sign Up")
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Please check your email",
                modifier = Modifier.fillMaxWidth(), // to  align it to the left
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            NormaleTexte("We’ve sent a code to $email")

            Spacer(modifier = Modifier.height(40.dp))

            // Appelle la fonction de champ OTP et passe la liste et la fonction de mise à jour
            OtpTextField(
                otp = otp.value,
                onOtpChange = { index, newValue ->
                    otp.value = otp.value.toMutableList()
                        .apply { this[index] = newValue } // Mise à jour de la valeur OTP
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Add the "Send OTP Again" text button

            LinkText(value = "Send code again") {
                resendOtp() // Call the resend OTP logic
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                val otpString = otp.value.joinToString("")  // Combine OTP list into string

                composableScope.launch(Dispatchers.IO) {
                    try {
                        val result = supabaseClient.auth.verifyEmailOtp(
                            email = email,
                            token = otpString,
                            type = OtpType.Email.SIGNUP
                        )

                        // Navigate to the next screen if verification is successful
                        withContext(Dispatchers.Main) {
                            navController.navigate("SignUp4Photo")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Verification failed"
                        }
                    }
                }
            } ,modifier = Modifier
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


            if (errorMessage.value.isNotEmpty()) {
                Text(errorMessage.value, color = Color.Red)
            }


            Spacer(modifier = Modifier.height(10.dp))

            HaveAccount(navController)


        }
    }
}





