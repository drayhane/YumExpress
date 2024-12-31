import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import com.example.fooddelivery.R
import com.example.fooddelivery.data.local.OrderEntity

@Composable
fun DisplayOrders(navController: NavHostController, orders: List<OrderEntity>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Barre supérieure
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Action de retour */ }) {
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

        // Liste des commandes avec Divider
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(orders) { order ->
                OrderItem(order = order,navController)
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun OrderItem(order: OrderEntity, navController: NavHostController) {
    // Couleur dynamique en fonction du statut
    val statusColor = when (order.status) {
        "Delivered" -> Color(0xFF4CAF50) // Vert
        "Canceled" -> Color(0xFFF44336) // Rouge
        else -> Color.Gray // Par défaut
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("details")  }, // Navigation sur clic
        verticalAlignment = Alignment.CenterVertically,

    ) {
        // Image de la commande
        Image(
            painter = painterResource(id = order.imageRes),
            contentDescription = "Order Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Détails de la commande
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = order.restaurantName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${order.totalPrice}  |",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = order.date,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = order.status,
                fontSize = 12.sp,
                color = statusColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
