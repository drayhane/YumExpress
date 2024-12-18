package com.example.fooddelivery.screens.ForgotPassword


import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
@Composable
fun ForgotPasswordOTP (navController: NavController){

    fun resendOtp() {
        // Here you would handle the OTP resend logic
        // For now, we just print to the console
        println("OTP sent again")
    }



    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()// to vocer the whole screen

    ){

        Column (

            modifier = Modifier
                .fillMaxSize() // Makes the Column fill the entire screen
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            verticalArrangement = Arrangement.Center, // Center vertically
            //verticalArrangement = Arrangement.Center // Center vertically
        ){
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Please check your email")
            Spacer(modifier = Modifier.height(5.dp))
            NormaleTexte("We’ve sent a code to helloworld@gmail.com")


            Spacer(modifier = Modifier.height(40.dp))
            // Appelle la fonction de champ OTP et passe la liste et la fonction de mise à jour
            val otp = remember { mutableStateOf(MutableList(4) { "" }) }
            OtpTextField(
                otp = otp.value,
                onOtpChange = { index, newValue ->
                    otp.value = otp.value.toMutableList().apply { this[index] = newValue } // Mise à jour de la valeur OTP
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Add the "Send OTP Again" text button
            LinkText(value = "Send code again") {
                resendOtp() // Call the resend OTP logic
            }
            Spacer(modifier = Modifier.height(100.dp))
            Spacer(modifier = Modifier.weight(1f))

            ButtonComponent("Next",navController,"ForgotPassword2")



        }
    }
}