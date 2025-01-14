package com.example.fooddelivery.ui.screens
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.domain.respository.CartRepository
import com.example.fooddelivery.domain.respository.CartRepositoryImpl
import com.example.fooddelivery.domain.respository.ComposeRepository
import com.example.fooddelivery.domain.respository.ComposeRepositoryImpl
import com.example.fooddelivery.domain.respository.ItemRespository
import com.example.fooddelivery.domain.respository.ItemRespositoryImpl
import com.google.gson.Gson
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import supabaseClient

@Composable
fun Displaymeal(navController: NavHostController, ItemId: String) {
    val ItemRespository: ItemRespository = ItemRespositoryImpl()
    val cartRepository: CartRepository = CartRepositoryImpl()
    val composerepository: ComposeRepository = ComposeRepositoryImpl()
    val item: Item = runBlocking(Dispatchers.IO) {
        ItemRespository.getItemById(ItemId)!!
    }

    val currentUser = supabaseClient.auth.currentUserOrNull()
    val UserId = currentUser?.id ?: throw Exception("User not authenticated")

    var showDialog by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("Simple") }
    val selectedSauces = remember { mutableStateListOf<String>() }
    var quantity by remember { mutableStateOf(1) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    var mealAdded by remember { mutableStateOf(false) }

    if (mealAdded) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("Meal added to cart!")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {

                Image(
                    painter = rememberAsyncImagePainter(model = item.image),
                    contentDescription = "Top Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "Arrow Left Icon"
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-15).dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center

            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        item.name?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Text(
                            text = "${item.price} DA",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFFFF640D)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Ingredients", style = MaterialTheme.typography.titleMedium)
                    item.ingredient?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(modifier = Modifier.padding(vertical = 6.dp))

                    Spacer(modifier = Modifier.height(6.dp))

                    val sizeOptions = mapOf(
                        "Burger" to listOf("Simple", "Double", "Mega"),
                        "Tacos" to listOf("M", "L", "XL"),
                        "Pizza" to listOf("M", "L", "XL")
                    )

                    Text(text = "Size", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Select the sizing of your item.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    if (!item.Type.isNullOrEmpty()) {
                        sizeOptions[item.Type]?.let { sizes ->
                            if (selectedSize == "Simple" && sizes.isNotEmpty()) {
                                selectedSize = sizes.first()
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                sizes.forEach { size ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = selectedSize == size,
                                            onClick = { selectedSize = size }
                                        )
                                        Text(text = size)
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(modifier = Modifier.padding(vertical = 6.dp))

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Sauces", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Select up to 3 sauces",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.padding(vertical = 0.dp) // Espacement minimal entre les lignes
                    )
                    {
                        listOf("Mayonnaise sauce", "Spicy sauce", "BBQ sauce").forEach { sauce ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 1.dp) // Adjust vertical spacing here
                            ) {
                                Checkbox(
                                    checked = selectedSauces.contains(sauce),
                                    onCheckedChange = {
                                        if (selectedSauces.contains(sauce)) {
                                            selectedSauces.remove(sauce)
                                        } else if (selectedSauces.size < 3) {
                                            selectedSauces.add(sauce)
                                        }
                                    }
                                )
                                Text(
                                    text = sauce,
                                    modifier = Modifier.padding(start = 4.dp)
                                ) // Add small padding if needed
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(modifier = Modifier.padding(vertical = 6.dp))


                    Text(text = "Additional notes", style = MaterialTheme.typography.titleMedium)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .border(0.5.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        BasicTextField(
                            value = notes,
                            onValueChange = { newValue -> notes = newValue },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 16.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Divider(modifier = Modifier.padding(vertical = 6.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFF640D))
                                .height(46.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text(
                                    text = "-",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "$quantity",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))

//
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1F1F1F))
                                .height(46.dp)
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    Log.d("Displaymeal", "Ajouter au panier cliqué")
                                    runBlocking {
                                        val activeCart = cartRepository.Getactivecart(UserId)
                                        Log.d("Displaymeal", "Panier actif: $activeCart")

                                        if (activeCart == null || activeCart.Id_rest != item.id_restaurant) {
                                            if (activeCart == null) {
                                                val newCartId = cartRepository.CreateCart(
                                                    UserId,
                                                    item.id_restaurant
                                                )
                                                val cmp = newCartId?.let {
                                                    it.id_card?.let { it1 ->
                                                        compose(
                                                            id_item = ItemId,
                                                            id_card = it1, // Utilisation de it.id_card si newCartId n'est pas nul
                                                            quantity = quantity.toString(),
                                                            sauce = selectedSauces.joinToString(", "),
                                                            size = selectedSize,
                                                            note = notes.text
                                                        )
                                                    }
                                                }
                                                if (cmp != null) {
                                                    cmp.id_card = newCartId.id_card.toString()
                                                }
                                                if (cmp != null) {
                                                    composerepository.additemtocart(cmp, item.price)
                                                    mealAdded = true

                                                }
                                            } else {
                                                if (activeCart.Id_rest == null) {

                                                    activeCart.id_card?.let {
                                                        cartRepository.modifrest(
                                                            it,
                                                            item.id_restaurant
                                                        )
                                                    }
                                                    val cmp = activeCart.id_card?.let {
                                                        compose(
                                                            id_item = ItemId,
                                                            id_card = it,
                                                            quantity = quantity.toString(),
                                                            sauce = selectedSauces.joinToString(", "),
                                                            size = selectedSize,
                                                            note = notes.text

                                                        )
                                                    }
                                                    if (cmp != null) {
                                                        composerepository.additemtocart(
                                                            cmp,
                                                            item.price
                                                        )
                                                        mealAdded = true

                                                    }

                                                } else {
                                                    showDialog = true
                                                }
                                            }

                                        } else {
                                            Log.d("Displaymeal", "Ajout de l'article au panier")
                                            val cmp = activeCart.id_card?.let {
                                                compose(
                                                    id_item = ItemId,
                                                    id_card = it,
                                                    quantity = quantity.toString(),
                                                    sauce = selectedSauces.joinToString(", "),
                                                    size = selectedSize,
                                                    note = notes.text

                                                )
                                            }
                                            if (cmp != null) {
                                                composerepository.additemtocart(cmp, item.price)
                                                mealAdded = true

                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF1F1F1F
                                    )
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.panier), // Replace with your image resource ID
                                        contentDescription = "Custom Image Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte
                                    Text(
                                        text = "Add to cart",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

// AlertDialog
    if (showDialog) {
        AlertDialog(

            onDismissRequest = { showDialog = false },
            title = { Text("Active cart\n") },
            text = { Text("Your cart belongs to a different restaurant. Would you like to replace it?") },
            confirmButton = {
                Button(onClick = {
                    runBlocking {

                        val activeCart = cartRepository.Getactivecart(UserId)
                        activeCart?.let {

                            it.id_card?.let { it1 -> cartRepository.Finirpanier(it1) }
                            val newCartId =
                                cartRepository.CreateCart(UserId, item.id_restaurant!!)!!.id_card


                            val cmp = compose(
                                id_item = ItemId,
                                id_card = newCartId ?: "0",
                                quantity = quantity.toString(),
                                sauce = selectedSauces.joinToString(", "),
                                size = selectedSize,
                                note = notes.text

                            )
                            composerepository.additemtocart(cmp, item.price)
                            mealAdded = true

                        }
                    }
                    showDialog = false
                }) {
                    Text("Replace")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800),
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    if (mealAdded) {
        val idresrt = Gson().toJson(item.id_restaurant)
        navController.navigate("RestaurantScreen/${idresrt}")
    } // Handle the card click

}




fun onReplaceCart(
    composeRepository: ComposeRepository,
    cartRepository: CartRepository,
    cartid: String,
    userId: String,
    restaurantId: String,
    cmp: compose,
    item_price: Double,
    onMealAdded: (Boolean) -> Unit // callback pour changer l'état de mealAdded

) {
    runBlocking {
        try {
            Log.d("onReplaceCart", "Finalisation de l'ancien panier: $cartid")
            cartRepository.Finirpanier(cartid)
            val newCartId = cartRepository.CreateCart(userId, restaurantId)
            Log.d("onReplaceCart", "Nouveau panier créé: $newCartId")
            cmp.id_card = newCartId.toString()
            composeRepository.additemtocart(cmp, item_price)
            onMealAdded(true)
        } catch (e: Exception) {
            Log.e("onReplaceCart", "Erreur lors du remplacement du panier: ${e.message}")
        }
    }
}
