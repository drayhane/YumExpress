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
import com.example.fooddelivery.R
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.NormaleBoldTexte
import com.example.fooddelivery.components.NormaleTexte
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField

@Composable
fun SignUp1 (){
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()// to vocer the whole screen
            .padding(16.dp)

    ){

        Column (
            modifier = Modifier
                .fillMaxSize(), // Makes the Column fill the entire screen
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            //verticalArrangement = Arrangement.Center // Center vertically
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo background", // A description for accessibility
                modifier = Modifier
                    .width(250.dp)  // Set width
                    .height(100.dp) // Set height
            )

            Spacer(modifier = Modifier.height(20.dp))
            TitleTexte("Log in")
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField("Email address")
            passwordTextField("Password")


            Spacer(modifier = Modifier.weight(1f))
            Row (){
                Text(
                    text = "Don't have an account? ",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,

                        )
                )

                Text(
                    text = "Sign up",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Normal,

                        )
                )
            }


        }
    }
}

@Preview
@Composable
fun DefaulPreviewOfSignUp1(){
    SignUp1()
}
