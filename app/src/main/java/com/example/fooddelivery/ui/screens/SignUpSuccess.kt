package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.R
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.ButtonComponent
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.TitleTexte

@Composable
fun SignUpSuccess (navController: NavController){
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

            TitleTexte("Your account was succefully created !")
            Spacer(modifier = Modifier.height(5.dp))
            NormaleTexte("Your next meal is just a click away!")
            Spacer(modifier = Modifier.height(120.dp))

            Image(
                painter = painterResource(id = R.drawable.man),
                contentDescription = "Logo background", // A description for accessibility
                modifier = Modifier.size(290.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent("Continue",navController,"HomeScreen") // should go to the acceuil
        }}
}