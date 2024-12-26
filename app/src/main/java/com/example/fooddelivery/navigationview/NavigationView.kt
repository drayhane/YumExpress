package com.example.fooddelivery.navigationview

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import authentification
import com.example.fooddelivery.screens.ForgotPassword.ForgotPassword1
import com.example.fooddelivery.screens.ForgotPassword.ForgotPassword2
import com.example.fooddelivery.screens.ForgotPassword.ForgotPasswordOTP
import com.example.fooddelivery.screens.SignUp1
import com.example.fooddelivery.screens.SignUp2
import com.example.fooddelivery.screens.SignUp3OTP
import com.example.fooddelivery.screens.SignUp4Photo
import com.example.fooddelivery.screens.SignUpSuccess
import com.example.fooddelivery.screens.WelcomePages.LogoPage
import com.example.fooddelivery.screens.WelcomePages.Welcome1

@Composable
fun NavigationView() {
    val navController = rememberNavController()

    NavHost(navController= navController, startDestination = "LogIn"){// normalement Welcome1   SignUp3OTP
        composable("LogIn"){ SignUp1(navController) }
        composable("SignUp2"){ SignUp2(navController) }
       // composable("SignUp3OTP"){ SignUp3OTP(navController) }
        composable("SignUp4Photo"){ SignUp4Photo(navController) }
        composable("SignUpSuccess"){ SignUpSuccess(navController) }
        composable("ForgotPassword1"){ ForgotPassword1(navController) }
        composable("ForgotPassword2"){ ForgotPassword2(navController) }
        composable("ForgotPasswordOTP"){ ForgotPasswordOTP(navController) }
        composable("Welcome1"){ Welcome1(navController) }
        composable("LogoPage"){ LogoPage(navController) }
        composable("authentification"){ authentification(navController) } // the one i used to test supabase with cart

        composable("SignUp3OTP?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            SignUp3OTP(navController, email)
        }





    }
}
