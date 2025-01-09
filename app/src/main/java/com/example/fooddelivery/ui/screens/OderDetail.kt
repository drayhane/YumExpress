

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.data.model.order1
import com.example.fooddelivery.domain.respository.ItemRespository
import com.example.fooddelivery.domain.respository.ItemRespositoryImpl
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Displaydetail(navController: NavHostController, order: order1, products: List<compose>,totalPrice:Double) {
    val restaurantRepository: RestaurantRepository = RestaurantRepositoryImpl()
    val itemRepository: ItemRespository = ItemRespositoryImpl()
    val restRepository:RestaurantRepository=RestaurantRepositoryImpl()

    val restaurantState = remember { mutableStateOf<Restaurant?>(null) }

    // Coroutine to fetch the restaurant
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(order.Id_rest) {
        order.Id_rest?.let {
            restaurantState.value = restaurantRepository.getRestaurantById(it)
        }
    }
    val delivery: String? =restaurantState.value?.delivery_price

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
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
                    text = "Order Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Main content in LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Restaurant Image and Info
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(model = restaurantState.value?.logo),
                            contentDescription = "Restaurant Image",
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Text(
                                text = restaurantState.value?.name ?: "Loading...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = buildAnnotatedString {
                                    append("Check shop's details →")
                                    addStyle(
                                        SpanStyle(
                                            color = Color(0xFFFF9800),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal
                                        ), 0, length)  // Styling the entire string

                                    addStyle(SpanStyle(textDecoration = TextDecoration.Underline), 0, length)
                                },
                                modifier = Modifier.clickable {
                                    val idresrt= Gson().toJson(order.Id_rest)
                                    navController.navigate("RestaurantScreen/${idresrt}") } // Handle the card click                                }
                            )
                        }
                    }
                }

                // Order Status
                item {
                    order.status?.let {
                        Text(
                            text = it,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                // Order Information
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Order date:",
                                fontSize = 14.sp
                            )
                            Text(
                                text = order.datee,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Payment type:",
                                fontSize = 14.sp
                            )
                            Text(
                                text = order.payent_meth,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // la list des productsq
                item {
                    Text(
                        text = "Products",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Product Items
                items(products) { product ->
                    val itemState = remember { mutableStateOf<Item?>(null) }
                    coroutineScope.launch {
                        itemState.value = itemRepository.getItemById(product.id_item)

                    }
                    itemState.value?.let { nonNullItem ->
                        ProductItem(nonNullItem.name ?: "", product.quantity.toString(), nonNullItem.image ?: "")
                    }
                }

                // Order Details
                item {
                    Column {
                        Text(
                            text = "Order details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal", fontSize = 14.sp)
                            Text(text = "$totalPrice DA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Platform fees", fontSize = 14.sp)
                            Text(text = "100 DA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Delivery charges", fontSize = 14.sp)

                            Text(text = "$delivery DA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Divider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val total = totalPrice + (delivery?.toDouble() ?: 0.0) + 100

                            Text(
                                text = "total",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$total DA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(name: String, quantity: String, imageRes: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageRes),
            contentDescription = "Product Image",
            modifier = Modifier
                .size(60.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "$quantity * $name",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
