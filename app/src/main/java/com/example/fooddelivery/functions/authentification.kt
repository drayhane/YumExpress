import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch


@Serializable
data class Cart(
    val id_card: String,
    val total_price: Double,
    val food_note: String
)

@Composable
fun authentification(navController: NavController) {

    // Using a state variable to trigger re-composition when data is fetched
    val carts = remember { mutableStateListOf <Cart>() }

    // Fetch the data asynchronously when the composable is first launched
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
                // Make the request to Supabase
                val results = supabaseClient.from("cart").select().decodeList<Cart>()
                carts.addAll(results)

        }
    }


    // Display the list of carts
    Column {
        LazyColumn {
            items(carts) { cart ->
                ListItem(
                    headlineContent = {
                        Text(text = cart.food_note)
                    },

                    )
            }
        }

        // insert into cart
        var newCart by remember { mutableStateOf("") }
        val composableScope = rememberCoroutineScope()

        var foodNote by remember { mutableStateOf("") }
        var idCard by remember { mutableStateOf("") }
        var totalPrice by remember { mutableStateOf("") }

        Row {
            Column {

                OutlinedTextField(
                    value = foodNote,
                    onValueChange = { foodNote = it },
                    label = { Text("Food Note") }
                )
                OutlinedTextField(
                    value = idCard,
                    onValueChange = { idCard = it },
                    label = { Text("ID Card") }
                )
                OutlinedTextField(
                    value = totalPrice,
                    onValueChange = { totalPrice = it },
                    label = { Text("Total Price") }
                )

                Button(onClick = {
                    composableScope.launch(Dispatchers.IO) {
                        val cart = supabaseClient.from("cart").insert(
                            mapOf(
                                "id_card" to idCard,
                                "total_price" to totalPrice,
                                "food_note" to foodNote,
                            )
                        ) {
                            select()
                            single()
                        }.decodeAs<Cart>()
                        carts.add(cart)
                        newCart = ""
                    }
                })
                {
                    Text("Save")
                }
            }
        }

    }
}


/*
@Composable
fun authentification(navController: NavController) {

    // Using a state variable to trigger re-composition when data is fetched
    var carts by remember { mutableStateOf<List<Cart>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var noDataMessage by remember { mutableStateOf<String?>(null) }

    // Fetch the data asynchronously when the composable is first launched
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                // Log a message to confirm that the data fetch is initiated
                println("Fetching data from Supabase...")

                // Make the request to Supabase
                val results = supabaseClient.from("cart").select().decodeList<Cart>()
                println("Results: $results") // Check the output

                // Log the results to confirm data was fetched
                println("Data fetched: $results")

                // Check if data is null or empty
                if (results.isEmpty()) {
                    // If the results are empty, display a "No data found" message
                    noDataMessage = "No data available."
                } else {
                    // If data is returned, update the carts list
                    carts = results
                }

            } catch (e: Exception) {
                // Log any errors
                println("Error fetching data: ${e.localizedMessage}")

                // Show an error message
                errorMessage = "Error fetching data: ${e.localizedMessage}"
            }
        }
    }

    // If there's an error, show the error message
    if (errorMessage != null) {
        Text(text = "Error: $errorMessage")
    }

    // If there's no data, show the no data message
    if (noDataMessage != null) {
        Text(text = noDataMessage!!)
    }

    // Display the list of carts
    Column {
        LazyColumn {
            items(carts) { cart ->
                ListItem(
                    headlineContent = {
                        Text(text = cart.food_note)
                    },

                    )
            }
        }

        // insert into cart
        var newCart by remember { mutableStateOf("") }
        val composableScope = rememberCoroutineScope()
        Row{
            OutlinedTextField(value = newCart, onValueChange = {newCart = it})
            Button(onClick =  {
                composableScope.launch(Dispatchers.IO){
                    val cart = supabaseClient.from("cart").insert(mapOf("food_note" to newCart)){
                        select()
                        single()
                    }.decodeList<Cart>()
                    carts.add(cart)
                    newCart= ""
                }
            })
            {
                Text("Save")
            }
        }

    }
}
*/