package com.example.fooddelivery.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.R
import com.example.fooddelivery.components.LogoutButton
import com.example.fooddelivery.data.model.User1
import com.example.fooddelivery.domain.respository.UserRepository
import com.example.fooddelivery.domain.respository.UserRepositoryImpl
import com.example.fooddelivery.navigationview.BottomNavigationBar
import com.example.fooddelivery.ui.theme.Black1F
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import supabaseClient


@Composable

fun DisplayProfil(navController: NavHostController) {
    val currentUser = supabaseClient.auth.currentUserOrNull()
    val userId = currentUser?.id ?: throw Exception("User not authenticated")
    val userRepository: UserRepository = UserRepositoryImpl()

    val user: User1 = runBlocking(Dispatchers.IO) {
        userRepository.getUserById(userId)!!
    }

    Scaffold(

        content = { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF8F8F8))
                .clickable  {
                    navController.popBackStack()
                },
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
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Photo de profil
            Box(contentAlignment = Alignment.BottomEnd) {
                if (user.profile_picture.isNullOrEmpty()) {
                    // Afficher l'image par défaut
                    Image(
                        painter = painterResource(id = R.drawable.profil),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = user.profile_picture
                        ),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("EditProfil")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFA500))
                            .padding(4.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Afficher les informations utilisateur
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Email address
            CustomTextField(label = "Email address", value = user.email)

            Spacer(modifier = Modifier.height(16.dp))

            // Address
            CustomTextField(label = "Address", value = user.adress ?: "",)

            Spacer(modifier = Modifier.height(16.dp))

            // Phone number
            CustomTextField(label = "Phone number", value = user.num_tel?: "")

            Spacer(modifier = Modifier.weight(1f))

            LogoutButton(navController)

        }
    }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(label: String, value: String?) {
    val displayValue = value ?: "" // Si la valeur est null, on affiche une chaîne vide

    OutlinedTextField(
        value = displayValue,
        onValueChange = {}, // Pas de changement du texte
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFFFFA500), // Bordure focus
            unfocusedBorderColor = Color.LightGray  // Bordure sans focus
        )
    )
}
