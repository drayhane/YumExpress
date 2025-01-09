package com.example.fooddelivery.ui.screens

import RestaurantRepository
import RestaurantRepositoryImpl
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
fun DisplayPanier(navController: NavHostController, userId: String) {
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
    var activeCart: Cart? = null

    fun recalculateTotal() {
        totalPrice = products.sumOf { it.price * it.quantity }.toDouble()
    }
    LaunchedEffect(userId) {
        try {
            activeCart = cartRepository.Getactivecart(userId)
            activeCart?.id_card?.let { cartId ->
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
        } catch (e: Exception) {
            println("Error loading products: ${e.message}")
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ret_btn),
                    contentDescription = "Back Button",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
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
                    text = location,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Change location icon
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_forward),
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
                            coroutineScope.launch {
                                composeRepository.deletefrompanier(
                                    productToDelete.id,
                                    productToDelete.id_Cart,
                                    totalPrice
                                )
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
                        coroutineScope.launch {
                            val idRestaurant = products.firstOrNull()?.id_restaurant ?: "Unknown"
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            val currentDateTime = LocalDateTime.now().format(formatter).toString()
                            val idCart = products.firstOrNull()?.id_Cart ?: "Unknown"
                            val newOrder = order1(
                                id_order = "order_${System.currentTimeMillis()}",
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
    }
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
                modifier = Modifier.size(36.dp) // Taille compacte pour le bouton
            ) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }


        // Bouton de suppression
        IconButton(
            onClick = onDelete,
            modifier = Modifier.weight(0.5f) // Réduit l'espace utilisé par ce bouton
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
