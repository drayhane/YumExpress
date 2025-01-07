package com.example.fooddelivery


import DisplayFavorits
import DisplayOrders
import Displaydetail
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.data.model.order1
import com.example.fooddelivery.ui.screens.DisplayEdit
import com.example.fooddelivery.ui.screens.DisplayPanier
import com.example.fooddelivery.ui.screens.DisplayProfil
import com.example.fooddelivery.ui.screens.Displaymeal
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the theme and UI content of the app
        setContent {
            FoodDeliveryTheme{
                val navController = rememberNavController()
                main(navController)

            }
        }
    }
}
@Composable
fun  main(navcontroller : NavHostController)
{ val userId = "1"
    val itemid="2"

    var startDestination = "panier"
    NavHost(navController = navcontroller, startDestination ){
        composable ("Profil")   {

                DisplayProfil(navcontroller, userId)

        }
        composable("EditProfil"){DisplayEdit(navcontroller,userId)}
        composable("Orders")    {DisplayOrders(navcontroller,userId)}
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
            Displaydetail(navcontroller, order, products, totalPrice)
        }
        composable("favorits")  {DisplayFavorits(navcontroller)}
        composable("meal")      {Displaymeal(navcontroller,itemid,userId)}
        composable("panier")    {DisplayPanier(navcontroller,userId)}
        }
    }


