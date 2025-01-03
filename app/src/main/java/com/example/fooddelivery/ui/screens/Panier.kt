package com.example.fooddelivery.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fooddelivery.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class Product(
    val name: String,
    val price: Int,
    val quantity: Int
)

val Orange500 = Color(0xFFFF5722)
val GrayBackground = Color(0xFFF8F8F8)
val TextPrimary = Color.Black
val TextSecondary = Color.Gray

@Composable
fun DisplayPanier(navController: NavHostController) {
    var products by remember {
        mutableStateOf(
            listOf(
                Product("New York", 450, 1),
                Product("Dallas", 500, 2),
                Product("Miami", 550, 2)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Handle back navigation */ }) {
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

        // Delivery Location
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Orange500
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delivery location", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Bab Ezzouar Rue 02",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Products Section
        Text("Products", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ProductList(
            products = products,
            onDelete = { product -> products = products.filter { it != product } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Order Details
        Text("Order details", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OrderDetailItem("Subtotal", "1200 DA")
        OrderDetailItem("Platform fees", "100 DA")
        OrderDetailItem("Delivery charges", "195 DA")
        OrderDetailItem("Total", "1495 DA", isBold = true)

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method
        PaymentMethodSelection()

        Spacer(modifier = Modifier.height(16.dp))

        // Finalize Order Button
        Button(
            onClick = { /* Handle finalize order */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Finalize Order")
        }
    }
}
@Composable
fun ProductItem(
    name: String,
    price: Int,
    initialQuantity: Int,
    onDelete: () -> Unit
) {
    var quantity by remember { mutableStateOf(initialQuantity) }
    val offsetX = remember { Animatable(0f) }
    val swipeThreshold = 300f
    var isDeleteVisible by remember { mutableStateOf(false) } // Affiche ou non la corbeille

    // Scope de coroutine pour les appels suspendus
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            // Si le glissement dépasse le seuil vers la gauche, la corbeille reste visible
                            if (offsetX.value < -swipeThreshold) {
                                isDeleteVisible = true
                            } else {
                                // Sinon, cache la corbeille
                                isDeleteVisible = false
                            }
                            // Revenir à la position initiale
                            offsetX.animateTo(0f)
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            // Applique le mouvement à l'élément tout en limitant le mouvement
                            val newOffset = offsetX.value + dragAmount
                            offsetX.snapTo(newOffset.coerceIn(-swipeThreshold, 0f))
                        }
                    }
                )
            }
    ) {
        // Affichage du fond avec la corbeille si nécessaire
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), // Fond blanc
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isDeleteVisible) {
                // Si la corbeille est visible, on affiche l'icône de suppression
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .clickable {
                            onDelete()
                            isDeleteVisible = false  // Cache la corbeille après la suppression
                        }
                )
            }
        }

        // Affichage des détails du produit à l'avant
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .background(Color.White)  // Fond blanc pour le produit
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.burger),
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
                Text("$price DA", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Bouton de réduction de la quantité
                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text("$quantity", style = MaterialTheme.typography.bodyMedium)
                // Bouton d'augmentation de la quantité
                IconButton(onClick = { quantity++ }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }
    }
}



@Composable
fun ProductList(products: List<Product>, onDelete: (Product) -> Unit) {
    Column {
        products.forEach { product ->
            ProductItem(
                name = product.name,
                price = product.price,
                initialQuantity = product.quantity,
                onDelete = { onDelete(product) }
            )
        }
    }
}

@Composable
fun OrderDetailItem(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium)
        Text(value, style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun PaymentMethodSelection() {
    val paymentMethods = listOf("CASH", "EDAHABIA")
    val selectedMethod = remember { mutableStateOf("CASH") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Payment Method",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        paymentMethods.forEach { method ->
            val isSelected = method == selectedMethod.value
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        if (isSelected) GrayBackground else Color.Transparent,
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
