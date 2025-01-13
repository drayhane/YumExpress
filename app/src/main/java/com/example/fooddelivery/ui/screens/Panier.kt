package com.example.fooddelivery.ui.screens

import RestaurantRepository
import RestaurantRepositoryImpl
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Cart
import com.example.fooddelivery.data.model.order1
import com.example.fooddelivery.domain.respository.CartRepository
import com.example.fooddelivery.domain.respository.CartRepositoryImpl
import com.example.fooddelivery.domain.respository.ComposeRepository
import com.example.fooddelivery.domain.respository.ComposeRepositoryImpl
import com.example.fooddelivery.domain.respository.ItemRespository
import com.example.fooddelivery.domain.respository.ItemRespositoryImpl
import com.example.fooddelivery.domain.respository.OrderRespository
import com.example.fooddelivery.domain.respository.OrderRespositoryImpl
import com.example.fooddelivery.domain.respository.UserRepository
import com.example.fooddelivery.domain.respository.UserRepositoryImpl
import com.example.fooddelivery.ui.theme.Black1F
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import supabaseClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val image:String,
    val quantity: Int,
    val id_restaurant: String,
    val id_Cart: String
)

val Orange500 = Color(0xFFFF5722)
val TextPrimary = Color.Black
val TextSecondary = Color.Gray



@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("NewApi", "CoroutineCreationDuringComposition")
@Composable
fun DisplayPanier(navController: NavHostController, adress:String, lat: Double,long:Double) {
    val currentUser = supabaseClient.auth.currentUserOrNull()
    val userId = currentUser?.id ?: throw Exception("User not authenticated")
    val cartRepository: CartRepository = CartRepositoryImpl()
    val itemRepository: ItemRespository = ItemRespositoryImpl()
    val composeRepository: ComposeRepository = ComposeRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()
    val orderRepository: OrderRespository = OrderRespositoryImpl()
    val restRepository:RestaurantRepository=RestaurantRepositoryImpl()
    var products by remember { mutableStateOf(listOf<Product>()) }
    var totalPrice by remember { mutableStateOf(0.0) }
    var resultMessage by remember { mutableStateOf("") }
    var delivery by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var location by remember { mutableStateOf("") }
    val selectedMethod = remember { mutableStateOf("CASH") }
    var locationName by remember { mutableStateOf("Loading...") }
    val context = LocalContext.current
    var activeCart by remember { mutableStateOf<Cart?>(null) }
    var lat by remember { mutableStateOf( lat.toFloat())}
    var long by remember { mutableStateOf( long.toFloat()) }
    var lat2 by remember { mutableStateOf(0.0f) }
    var long2 by remember { mutableStateOf(0.0f) }
    var name by remember { mutableStateOf("Derbal Issam") }
    var num by remember { mutableStateOf("0663653874") }
    var res by remember { mutableStateOf("") }
    var id_res by remember { mutableStateOf("") }



// Launch a coroutine to get the current location name
    LaunchedEffect(Unit) {


            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        Log.i("Li GATINAHOM ", "ENDLatitude: $lat, Longitude: $long")
                        lat = if (lat != 0.0.toFloat()) lat.toFloat() else location.latitude.toFloat()
                        long = if (long != 0.0.toFloat()) long.toFloat() else location.longitude.toFloat()
                        Log.i("HELLOOOOOOOOOOOOOOOOOOO LocationInfo", "ENDLatitude: $lat, Longitude: $long")
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val addressLine = addresses[0].getAddressLine(0) ?: "No address line"
                            val country = addresses[0].countryName ?: "No country name"
                            locationName = "$addressLine, $country"

                        } else {
                            locationName = "Unknown Location"
                        }
                    } else {
                        locationName = "Unknown Location"
                    }
                }.addOnFailureListener {
                    locationName = "Failed to get location"
                }
            } else {
                locationName = "Permissions not granted"
            }
        }

    fun recalculateTotal() {
        totalPrice = products.sumOf { it.price * it.quantity }.toDouble()
    }
    LaunchedEffect(userId) {
        Log.i("HELLOOOOOOOOOOOOOOOOO UserID", "User ID: $userId")  // Log pour vérifier userId

        try {
            activeCart = cartRepository.Getactivecart(userId)

            // Vérifier si activeCart est nul ou si id_card est vide
            if (activeCart == null) {
                Log.w("CartStatus", "No active cart found for User ID: $userId")
            } else if (activeCart?.id_card.isNullOrEmpty()) {
                Log.w("CartStatus", "Active cart exists, but no id_card found for User ID: $userId")
            } else {
                // Le panier est valide et contient une id_card
                Log.i("CartStatus", "Active Cart: $activeCart, Cart ID: ${activeCart?.id_card}")
                activeCart?.id_card?.let { cartId ->
                    val id_restaurant = activeCart?.Id_rest ?: "1"
                    Log.i("HELLOOOOOOOOOOOOOOOOO Id_resto", "Restaurant ID: $id_restaurant")

                    val restaurant = restRepository.getRestaurantById(id_restaurant)
                    lat2 = restaurant?.lat ?: 0.0f
                    long2 = restaurant?.alongitude ?: 0.0f
                    Log.i("HELLOOOOOOOOOOOOOOOOO Id_resto", "Restaurant ID: $id_restaurant")
                    Log.i("HELLOOOOOOOOOOOOOOOOOOO LocationInfo", "Latitude: $lat2, Longitude: $long2")

                    name = restaurant?.delivery_man ?: "Derbal Issam"
                    num = restaurant?.num_delivery_man ?: "0663653874"
                    res= if (adress.isNotEmpty()) adress else locationName
                    id_res=restaurant?.id_restaurant ?: "/"

                    val composeProducts = composeRepository.getproducts(cartId)
                    val enrichedProducts = composeProducts?.mapNotNull { compose ->
                        itemRepository.getItemById(compose.id_item)?.let { item ->
                            item.image?.let {
                                Product(
                                    id = item.id_item,
                                    name = item.name ?: "Unknown",
                                    price = item.price.toInt(),
                                    image = it,
                                    quantity = compose.quantity.toInt(),
                                    id_restaurant = item.id_restaurant,
                                    id_Cart = cartId
                                )
                            }
                        }
                    } ?: listOf()

                    products = enrichedProducts
                    totalPrice = enrichedProducts.sumOf { it.price * it.quantity }.toDouble()
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Error loading products: ${e.message}")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8F8F8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Sharp.KeyboardArrowLeft,
                    contentDescription = "Call",
                    tint = Black1F,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable  {
                            navController.popBackStack()
                        }
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Panier",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(userId) {
            location = userRepository.GetUserLocation(userId).toString()
        }
         if (activeCart == null) {
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.White),
                 contentAlignment = Alignment.Center
             ) {
                 Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     verticalArrangement = Arrangement.Center
                 ) {
                     Icon(
                         imageVector = Icons.Default.ShoppingCart,
                         contentDescription = "Cart Icon",
                         tint = Orange500,
                         modifier = Modifier.size(64.dp)
                     )

                     Spacer(modifier = Modifier.height(16.dp))
                     Text(
                         "No active cart is available.",
                         style = MaterialTheme.typography.bodyMedium,
                         color = Color.Black
                     )
                 }
             }
        }
       else{
        // Delivery Location
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Orange500
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Delivery location",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = if (adress.isNotEmpty()) adress else locationName,

                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Change location icon
            IconButton(onClick = {navController.navigate("address_screen")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_forward),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Change Location",
                    tint = Orange500
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Divider(modifier = Modifier.padding(vertical = 6.dp))

        Spacer(modifier = Modifier.height(6.dp))
        // Products Section
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text("Products", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                if (products.isNotEmpty()) {
                    ProductList(
                        products = products,
                        totalPrice = totalPrice,
                        composeRepository = composeRepository,
                        onProductUpdate = { updatedProduct ->
                            products =
                                products.map { if (it.id == updatedProduct.id) updatedProduct else it }
                            recalculateTotal()
                        },
                        onDelete = { productToDelete ->
                            products = products.filter { it.id != productToDelete.id }
                            recalculateTotal()
                            // Vérifier si le panier est vide après la suppression du produit

                            coroutineScope.launch {
                                composeRepository.deletefrompanier(
                                    productToDelete.id,
                                    productToDelete.id_Cart,
                                    totalPrice
                                )
                                if (products.isEmpty()) {
                                    activeCart!!.id_card?.let { cartRepository.Finirpanier(it) }
                                    activeCart = null
                                }
                            }
                        }
                    )
                } else {
                    Text("Votre panier est vide.", color = TextSecondary)
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Divider(modifier = Modifier.padding(vertical = 6.dp))
            }

            item {
                Text("Order details", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OrderDetailItem("Subtotal", "$totalPrice DA")
                OrderDetailItem("Platform fees", "100 DA")
                GlobalScope.launch {
                    val price =
                        restRepository.getdeliveryprice(products.firstOrNull()?.id_restaurant ?: "")
                    delivery = price
                }
                OrderDetailItem(
                    "Delivery charges",
                    "$delivery DA"
                ) // get from restaurant delivery_price
                val finalPrice = totalPrice + 100 + (delivery?.toDouble() ?: 0.0)
                OrderDetailItem("Total", "$finalPrice DA", isBold = true, color = Orange500)
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Divider(modifier = Modifier.padding(vertical = 6.dp))
            }

            item {
                Text("Payment Method", style = MaterialTheme.typography.titleMedium)
                PaymentMethodSelection(selectedMethod = selectedMethod)
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Divider(modifier = Modifier.padding(vertical = 6.dp))
            }

            item {
                Button(
                    onClick = {
                        val idorder="order_${System.currentTimeMillis()}"
                        Log.d("hello","panier :$idorder")

                        coroutineScope.launch {
                            val idRestaurant = products.firstOrNull()?.id_restaurant ?: "Unknown"
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            val currentDateTime = LocalDateTime.now().format(formatter).toString()
                            val idCart = products.firstOrNull()?.id_Cart ?: "Unknown"
                            val newOrder = order1(
                                id_order = idorder,
                                datee = currentDateTime,
                                payent_meth = selectedMethod.value,
                                status = "Processing",
                                delivery_adress = location,
                                delevery_note = "",
                                id_user = userId,
                                Id_rest = idRestaurant,
                                id_card = idCart
                            )
                            try {
                                val isSuccess = orderRepository.neworder(newOrder)
                                resultMessage = if (isSuccess) {
                                    products = emptyList()
                                    totalPrice = 0.0
                                    cartRepository.Finirpanier(idCart)
                                    "Votre commande est en cours."
                                } else {
                                    "Une erreur est survenue lors de la commande."
                                }
                            } catch (e: Exception) {
                                resultMessage = "Une erreur est survenue : ${e.message}"
                            }
                        }
                       navController.navigate("tracking_screen?lat=${lat}&lon=${long}&lat2=${lat2}&lon2=${long2}&name=${name}&num=${num}&res=${res}&id_res=${id_res}&id_order=${idorder}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Finalize Order",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


        }
    }}
    if (resultMessage.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(resultMessage, color = if (resultMessage.startsWith("Votre commande")) Color.Green else Color.Red)
    }
}

@Composable
fun ProductItem(
    image: String,
    name: String,
    price: Int,
    itemId:String,
    cardId:String,
    initialQuantity: Int,
    totalPrice:Double,
    composeRepository:ComposeRepository,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var quantity by remember { mutableStateOf(initialQuantity) }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Image et détails du produit
        Row(
            modifier = Modifier
                .weight(2f) // Permet un espace relatif plus large
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Text("$price DA", style = MaterialTheme.typography.bodyMedium, color = Orange500)
            }
        }

        Spacer(modifier = Modifier.width(8.dp)) // Ajoute un espacement entre les colonnes

        // Gestion des quantités
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp), // Espacement contrôlé entre les éléments
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(8.dp)) // Fond noir avec coins arrondis
                .padding(horizontal = 12.dp, vertical = 6.dp) // Espacement interne
        ) {
            // Bouton "-"
            IconButton(
                onClick = {
                    if (quantity > 1) {
                        quantity -= 1
                        onQuantityChange(quantity)
                        val newTotalPrice = totalPrice - price.toDouble()
                        coroutineScope.launch {
                            composeRepository.modifquantity(itemId, cardId, quantity.toString(), newTotalPrice)
                        }
                    }
                },
                modifier = Modifier.size(36.dp) // Taille compacte pour le bouton
            ) {
                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            // Quantité
            Text(
                text = "$quantity",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White, // Texte en blanc
                textAlign = TextAlign.Center
            )

            // Bouton "+"
            IconButton(
                onClick = {
                    quantity += 1
                    onQuantityChange(quantity)
                    val newTotalPrice = totalPrice + price.toDouble()
                    coroutineScope.launch {
                        composeRepository.modifquantity(itemId, cardId, quantity.toString(), newTotalPrice)
                    }
                },
                modifier = Modifier.size(16.dp) // Taille compacte pour le bouton
            ) {
                Text("+", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }


        // Bouton de suppression
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .weight(0.5f),// Réduit l'espace utilisé par ce bouton
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Item")
        }
    }

}


@Composable
fun ProductList(
    products: List<Product>,
    totalPrice: Double,
    composeRepository: ComposeRepository,
    onProductUpdate: (Product) -> Unit,
    onDelete: (Product) -> Unit
) {
    Column {
        products.forEach { product ->
            ProductItem(
                image = product.image,
                name = product.name,
                price = product.price,
                itemId=product.id,
                cardId=product.id_Cart,
                initialQuantity = product.quantity,
                totalPrice= totalPrice,
                composeRepository= composeRepository,
                onQuantityChange = { newQuantity ->
                    onProductUpdate(product.copy(quantity = newQuantity))
                },
                onDelete = { onDelete(product) }
            )
        }
    }
}


@Composable
fun OrderDetailItem(
    label: String,
    value: String,
    isBold: Boolean = false,
    color: Color = TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Text(
            value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Composable
fun PaymentMethodSelection(selectedMethod: MutableState<String>) {
    val paymentMethods = listOf("CASH", "EDAHABIA")

    Column(modifier = Modifier.fillMaxWidth()) {
        paymentMethods.forEach { method ->
            val isSelected = method == selectedMethod.value
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        if (isSelected) Color(0xFFF8F8F8) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { selectedMethod.value = method }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = if (method == "CASH") R.drawable.money else R.drawable.creditcard),
                    contentDescription = method,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = method,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected",
                        tint = Orange500,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
