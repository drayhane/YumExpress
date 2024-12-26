package com.example.fooddelivery.screens

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
import androidx.compose.runtime.Composable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.fooddelivery.ui.theme.MainBlack
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supabaseClient
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.withContext


@Composable
fun SignUp1 (navController: NavController){

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }
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
            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
            Spacer(modifier = Modifier.height(4.dp))
            ControllerText("Forgot Password ?",navController,"ForgotPassword1")


            Spacer(modifier = Modifier.height(90.dp))
            Spacer(modifier = Modifier.weight(1f))


            Button(onClick = {

                // Handle input validation
                if (email.value.isBlank() || password.value.isBlank()) {
                    errorMessage.value = "All fields are required"
                    return@Button
                }

                // Call Supabase to log in
                composableScope.launch(Dispatchers.IO) {
                    try {
                        // Use Supabase Auth to sign in with Email
                        val result = supabaseClient.auth.signInWith(Email) {
                            this.email = email.value.trim()
                            this.password = password.value.trim()
                        }

                        // Switch to the Main thread to handle navigation
                        withContext(Dispatchers.Main) {
                            navController.navigate("SignUpSuccess") // Navigate to the Home screen or appropriate page
                        }

                    } catch (e: Exception) {
                        // Handle login errors
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Login failed. Please try again."
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
                    Text(text = "Log in")
                }
            }


            Spacer(modifier = Modifier.weight(1f))
            OrSeparator()

            GoogleLoginIn{(println("Google login clicked!"))}


            Spacer(modifier = Modifier.height(15.dp))
            DontHaveAccount(navController)


        }
    }

}

/*
@Composable
fun SignUp1 (navController: NavController){

    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }

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


            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
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

*/
/*
@Preview
@Composable
fun DefaulPreviewOfSignUp1(){
    SignUp1(navController: NavController)
}
*/