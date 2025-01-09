package com.example.fooddelivery.navigationview

import AddReviewUseCase
import DisplayFavorits
import DisplayOrders
import Displaydetail
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.data.model.order1
import com.example.fooddelivery.domain.respository.ReviewRespositoryImpl
import com.example.fooddelivery.domain.usecase.GetReviewUseCase
import com.example.fooddelivery.ui.screens.CartsScreen
import com.example.fooddelivery.ui.screens.DisplayEdit
import com.example.fooddelivery.ui.screens.DisplayPanier
import com.example.fooddelivery.ui.screens.DisplayProfil
import com.example.fooddelivery.ui.screens.Displaymeal
import com.example.fooddelivery.ui.screens.FavoritesScreen
import com.example.fooddelivery.ui.screens.ForgotPassword.ForgotPassword1
import com.example.fooddelivery.ui.screens.ForgotPassword.ForgotPassword2
import com.example.fooddelivery.ui.screens.ForgotPassword.ForgotPasswordOTP
import com.example.fooddelivery.ui.screens.HomeScreen
import com.example.fooddelivery.ui.screens.OrdersScreen
import com.example.fooddelivery.ui.screens.ProfileScreen
import com.example.fooddelivery.ui.screens.RestaurantScreen
import com.example.fooddelivery.ui.screens.SignUp1
import com.example.fooddelivery.ui.screens.SignUp2
import com.example.fooddelivery.ui.screens.SignUp3OTP
import com.example.fooddelivery.ui.screens.SignUp4Photo
import com.example.fooddelivery.ui.screens.SignUpSuccess
import com.example.fooddelivery.ui.screens.WelcomePages.LogoPage
import com.example.fooddelivery.ui.screens.WelcomePages.Welcome1
import com.example.fooddelivery.ui.screens.WelcomePages.userOrNull
import com.example.fooddelivery.ui.screens.Test
import com.google.gson.Gson
import reviewRespositoryImpl

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    val repositoryy = ReviewRespositoryImpl()
    val respo = reviewRespositoryImpl()
    val addReviewUseCase = AddReviewUseCase(respo)
    val getReviewUseCase = GetReviewUseCase(repositoryy)


    NavHost(navController= navController, startDestination = "Login"){// normalement Welcome1   SignUp3OTP

        composable(Destination.Home.route) { HomeScreen(navController = navController) }
        composable(Destination.Orders.route) { DisplayOrders(navController) }
        composable(Destination.Carts.route) {  DisplayPanier(navController) }
        composable(Destination.Favorites.route) { DisplayFavorits(navController) }
        composable(Destination.Profile.route) {  DisplayProfil(navController) }

        /////////////////////////////////////////////////////////////
        composable("LogIn"){ SignUp1(navController) }
        composable("SignUp2"){ SignUp2(navController) }
       // composable("SignUp3OTP"){ SignUp3OTP(navController) }
        composable("SignUp4Photo"){ SignUp4Photo(navController) }
        composable("SignUpSuccess"){ SignUpSuccess(navController) }
        composable("ForgotPassword1"){ ForgotPassword1(navController) }
        //composable("ForgotPassword2"){ ForgotPassword2(navController) }
        //composable("ForgotPasswordOTP"){ ForgotPasswordOTP(navController) }
        composable("Welcome1"){ Welcome1(navController) }
        composable("LogoPage"){ LogoPage(navController) }
       composable("Test"){ Test(navController) } // the logout button page
        composable("userOrNull"){ userOrNull(navController) } // the logout button page
        composable("HomeScreen"){
            HomeScreen(navController = navController)
        }
        composable("RestaurantScreen/{restaurantId}",
            arguments = listOf(
                navArgument("restaurantId") { type = NavType.StringType },
            )

        ){ backStackEntry ->
            val restaurantIdJson = backStackEntry.arguments?.getString("restaurantId")
            val restaurantId = Gson().fromJson(restaurantIdJson, String::class.java)

            RestaurantScreen(
                navController,
                context = LocalContext.current,
                addReviewUseCase = addReviewUseCase, restaurantId = restaurantId, getReviewUseCase = getReviewUseCase
            )
        }

        composable(
            "meal/{iditem}",
            arguments = listOf(
                navArgument("iditem") { type = NavType.StringType }
            ),
        ){ backStackEntry ->
            val iditemJson = backStackEntry.arguments?.getString("iditem")
            val iditem = Gson().fromJson(iditemJson, String::class.java)
            Displaymeal(navController,iditem)
        }



        composable("SignUp3OTP?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("email") ?: ""
            SignUp3OTP(
                navController, email, password)
        }


        composable("ForgotPasswordOTP?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPasswordOTP(navController, email)
        }

        composable("ForgotPassword2?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPassword2(navController, email)
        }

        composable ("Profil") {
            DisplayProfil(navController)
        }

        composable("EditProfil"){ DisplayEdit(navController) }
        composable("Orders")    {DisplayOrders(navController)}
        composable(
            "details/{order}/{products}/{totalPrice}",
            arguments = listOf(
                navArgument("order") { type = NavType.StringType },
                navArgument("products") { type = NavType.StringType },
                navArgument("totalPrice") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderJson = backStackEntry.arguments?.getString("order")
            val productsJson = backStackEntry.arguments?.getString("products")
            val totalPriceJson = backStackEntry.arguments?.getString("totalPrice")
            val order = Gson().fromJson(orderJson, order1::class.java)
            val products = Gson().fromJson(productsJson, Array<compose>::class.java).toList()
            val totalPrice = Gson().fromJson(totalPriceJson, Double::class.java)
            Displaydetail(navController, order, products, totalPrice)
        }
        composable("favorits")  {DisplayFavorits(navController)}
        composable("panier")    { DisplayPanier(navController) }





    }
}
