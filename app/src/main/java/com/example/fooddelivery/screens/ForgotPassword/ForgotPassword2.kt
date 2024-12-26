package com.example.fooddelivery.screens.ForgotPassword


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.ButtonComponent
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField

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
}