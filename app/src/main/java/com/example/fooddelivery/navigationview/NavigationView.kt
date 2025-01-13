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
import com.example.fooddelivery.ui.screens.AddressScreen
import com.example.fooddelivery.ui.screens.DeliverySuccessScreen
import com.example.fooddelivery.ui.screens.DisplayEdit
import com.example.fooddelivery.ui.screens.DisplayPanier
import com.example.fooddelivery.ui.screens.DisplayProfil
import com.example.fooddelivery.ui.screens.Displaymeal
import com.example.fooddelivery.ui.screens.ForgotPassword.ForgotPassword1
import com.example.fooddelivery.ui.screens.ForgotPassword.ForgotPassword2
import com.example.fooddelivery.ui.screens.ForgotPassword.ForgotPasswordOTP
import com.example.fooddelivery.ui.screens.HomeScreen
import com.example.fooddelivery.ui.screens.RestaurantScreen
import com.example.fooddelivery.ui.screens.SignUp1
import com.example.fooddelivery.ui.screens.SignUp2
import com.example.fooddelivery.ui.screens.SignUp3OTP
import com.example.fooddelivery.ui.screens.SignUp4Photo
import com.example.fooddelivery.ui.screens.SignUpSuccess
import com.example.fooddelivery.ui.screens.Test
import com.example.fooddelivery.ui.screens.TrackingScreen
import com.example.fooddelivery.ui.screens.WelcomePages.LogoPage
import com.example.fooddelivery.ui.screens.WelcomePages.Welcome1
import com.example.fooddelivery.ui.screens.WelcomePages.userOrNull
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
        composable("panier?adress={adress}&lat={lat}&long={lon}",
            arguments = listOf(
                navArgument("adress"){type=NavType.StringType},
                navArgument("lat") { type = NavType.StringType },
                navArgument("lon") { type = NavType.StringType })
        )
        { backStackEntry ->
            val adress = backStackEntry.arguments?.getString("adress") ?: ""
            val latString = backStackEntry.arguments?.getString("lat") ?: "0.0"
            val lonString = backStackEntry.arguments?.getString("lon") ?: "0.0"
            val lat = if (latString.isNotEmpty()) {
                latString.toDouble()
            } else {
                0.0 // or a default value if needed
            }

            val lon = if (lonString.isNotEmpty()) {
                lonString.toDouble()
            } else {
                0.0 // or a default value if needed
            }
            DisplayPanier(navController,adress,lat,lon)
        }

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
            SignUp3OTP(navController, email)
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


        composable("DeliverySuccessScreen?id_res={id_res}",
            arguments = listOf(
                navArgument("id_res") { type = NavType.StringType }))
        { backStackEntry ->
            val id_res = backStackEntry.arguments?.getString("id_res") ?: "1"

            DeliverySuccessScreen(
                navController = navController,
                id_res=id_res
            )
        }

        /////////////////////////////////////////////////////////////

            composable(
                route = "tracking_screen?lat={lat}&lon={lon}&lat2={lat2}&lon2={lon2}&name={name}&num={num}&res={res}&id_res={id_res}&id_order={id_order}",
                arguments = listOf(
                    navArgument("lat") { type = NavType.StringType },
                    navArgument("lon") { type = NavType.StringType },
                    navArgument("lat2") { type = NavType.StringType },
                    navArgument("lon2") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("num") { type = NavType.StringType },
                    navArgument("res") { type = NavType.StringType },
                    navArgument("id_res") { type = NavType.StringType },


                )
            ) { backStackEntry ->
                val latString = backStackEntry.arguments?.getString("lat") ?: "0.0"
                val lonString = backStackEntry.arguments?.getString("lon") ?: "0.0"
                val latString2 = backStackEntry.arguments?.getString("lat2") ?: "0.0"
                val lonString2 = backStackEntry.arguments?.getString("lon2") ?: "0.0"
                val name = backStackEntry.arguments?.getString("name") ?: "Unknow"
                val num = backStackEntry.arguments?.getString("num")?: "Unknow"
                val res = backStackEntry.arguments?.getString("res") ?: "Unknow"
                val id_res = backStackEntry.arguments?.getString("id_res")?: "Unknow"
                val id_order = backStackEntry.arguments?.getString("id_order")?: "Unknow"



                // Convert String to Double
                val lat = latString.toDouble()
                val lon = lonString.toDouble()
                val lat2= latString2.toDouble()
                val lon2 = lonString2.toDouble()



                TrackingScreen(
                    navController = navController,
                    endPointLat = lat,
                    endPointLon = lon,
                    startPointLat = lat2,
                    startPointLon = lon2,
                    name=name,
                    num=num,
                    res=res,
                    id_res = id_res,
                    id_order = id_order,
                )
            }
            composable("address_screen") {
                AddressScreen(
                    context = LocalContext.current,
                    navController = navController
                )
            }





    }
}
