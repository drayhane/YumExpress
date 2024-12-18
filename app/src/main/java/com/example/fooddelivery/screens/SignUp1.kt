package com.example.fooddelivery.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.example.fooddelivery.R
import com.example.fooddelivery.components.ButtonComponent
import com.example.fooddelivery.components.ControllerText
import com.example.fooddelivery.components.GoogleLoginIn
import com.example.fooddelivery.components.DontHaveAccount
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.OrSeparator
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField

@Composable
fun SignUp1 (navController: NavController){
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
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo background", // A description for accessibility
                modifier = Modifier
                    .width(290.dp)  // Set width
                    .height(100.dp) // Set height
            )

            Spacer(modifier = Modifier.height(40.dp))
            TitleTexte("Log in")
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField("Email address")
            passwordTextField("Password")
            Spacer(modifier = Modifier.height(4.dp))
            ControllerText("Forgot Password ?",navController,"ForgotPassword1")


            Spacer(modifier = Modifier.height(90.dp))
            Spacer(modifier = Modifier.weight(1f))

            ButtonComponent("Log in",navController,"")
            Spacer(modifier = Modifier.weight(1f))
            OrSeparator()

            GoogleLoginIn{(println("Google login clicked!"))}


            Spacer(modifier = Modifier.height(15.dp))
            DontHaveAccount(navController)


        }
    }
}


/*
@Preview
@Composable
fun DefaulPreviewOfSignUp1(){
    SignUp1(navController: NavController)
}
*/