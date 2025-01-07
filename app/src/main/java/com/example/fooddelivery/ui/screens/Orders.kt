
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.data.model.order1
import com.example.fooddelivery.data.model.restau
import com.example.fooddelivery.domain.respository.CartRepository
import com.example.fooddelivery.domain.respository.CartRepositoryImpl
import com.example.fooddelivery.domain.respository.ComposeRepository
import com.example.fooddelivery.domain.respository.ComposeRepositoryImpl
import com.example.fooddelivery.domain.respository.OrderRespository
import com.example.fooddelivery.domain.respository.OrderRespositoryImpl
import com.example.fooddelivery.domain.respository.restRepository
import com.example.fooddelivery.domain.respository.restRepositoryImpl
import com.google.gson.Gson
import kotlinx.coroutines.launch

data class OrderWithDetails(
    val order: order1,
    val totalPrice: Double,
    val products: List<compose>,
    val productCount: Int,
)



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DisplayOrders(navController: NavHostController, userid: String) {
    val orderRepository: OrderRespository = OrderRespositoryImpl()
    val composeRepository: ComposeRepository = ComposeRepositoryImpl()
    val coroutineScope = rememberCoroutineScope()
    val cartRepository: CartRepository = CartRepositoryImpl()
    val restaurantRepository: restRepository = restRepositoryImpl()
    var totalPrice=0.0
    // États pour gérer le chargement et les données
    val ordersWithDetails = remember { mutableStateOf<List<OrderWithDetails>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val fetchedOrders = orderRepository.Getorders(userid)
                val ordersDetails = fetchedOrders.map { order ->
                    val delivery= order.Id_rest?.let { restaurantRepository.getdeliveryprice(it) }
                    val cartResponse = order.id_card?.let { cartRepository.GetCart(it) }
                    val products = cartResponse?.id_card?.let { idCard ->
                        composeRepository.getproducts(idCard)
                    } ?: emptyList()
                    val productCount = products.size
                     totalPrice = cartResponse?.total_price ?: 0.0 + (delivery?.toDouble() ?: 0.0) +100// 100 c'est le plus de platfor TODO essaie de le metre comme global var
                    OrderWithDetails(order, totalPrice, products, productCount)
                }
                ordersWithDetails.value = ordersDetails
            } catch (e: Exception) {
                println("Error fetching orders: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Barre supérieure
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
                text = "Orders",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gestion de l'affichage
        when {
            isLoading.value -> {
                // Cercle de chargement
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black
                )
            }

            ordersWithDetails.value.isNotEmpty() -> {
                // Liste des commandes
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(ordersWithDetails.value) { orderWithDetails ->
                        OrderItem(orderWithDetails, navController, restaurantRepository)
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            else -> {
                // Message si aucune commande n'est trouvée
                Text(
                    text = "You don't have any orders yet.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OrderItem(order: OrderWithDetails, navController: NavHostController, restaurantRepository: restRepository) {
    // Couleur dynamique en fonction du statut
    val statusColor = when (order.order.status) {
        "Delivered" -> Color(0xFF4CAF50) // Vert
        "Canceled" -> Color(0xFFF44336) // Rouge
        else -> Color.Gray // Par défaut
    }

    // État pour stocker les informations du restaurant
    val restaurantState = remember { mutableStateOf<restau?>(null) }

    // Coroutine pour récupérer le restaurant
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(order.order.Id_rest) {
        order.order.Id_rest?.let {
            restaurantState.value = restaurantRepository.getRestaubyid(it)
        }
    }

    val total=order.totalPrice.toDouble()+ (restaurantState.value?.delivery_price?.toDouble()
        ?: 0.0)+100
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { val orderJson = Gson().toJson(order.order)
                val productsJson = Gson().toJson(order.products)

                 val totalprce=Gson().toJson(order.totalPrice)
                navController.navigate("details/$orderJson/$productsJson/$totalprce") }, // Navigation sur clic
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Image de la commande
        Image(
            painter = rememberAsyncImagePainter(model = restaurantState.value?.logo),
            contentDescription = "Restaurant Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Détails de la commande
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = restaurantState.value?.name ?: "Loading...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$total DA| ${order.productCount} products",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = order.order.datee,
                fontSize = 12.sp,
                color = Color.Gray
            )
            order.order.status?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
