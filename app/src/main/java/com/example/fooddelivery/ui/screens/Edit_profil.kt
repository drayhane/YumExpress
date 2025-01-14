package com.example.fooddelivery.ui.screens
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.R
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
fun DisplayEdit(navController: NavHostController) {
    val currentUser = supabaseClient.auth.currentUserOrNull()
    val userId = currentUser?.id ?: throw Exception("User not authenticated")



    val userRepository: UserRepository = UserRepositoryImpl()

    val user: User1 = runBlocking(Dispatchers.IO) {
        userRepository.getUserById(userId)!!
    }
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }
    var address by remember { mutableStateOf(user.adress) }
    var phone by remember { mutableStateOf(user.num_tel) }
    var profilePicture by remember { mutableStateOf(user.profile_picture) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    Scaffold(

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Back button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8F8F8))
                        .clickable {
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
                            .clickable {

                            }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))





























        val context = LocalContext.current
        val currentUser = supabaseClient.auth.currentUserOrNull()
        val userId = currentUser?.id ?: throw Exception("User not authenticated")
        val userRepository: UserRepository = UserRepositoryImpl()

        val userImageUri=user.profile_picture


        // State to track the selected image
        val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

        // Launchers for selecting images
        val launcherGallery = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                selectedImageUri.value = uri
            }
        }

        val launcherCamera = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            if (bitmap != null) {
                val tempUri = saveBitmapToUri(context, bitmap)
                selectedImageUri.value = tempUri
            }
        }

        // Circular profile picture display
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .clickable {
                    showPictureOptions(context, launcherGallery, launcherCamera)
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri.value != null) {
                // Show the selected image
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri.value),
                    contentDescription = "Selected Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            } else if (userImageUri != null) {
                if (userImageUri.isNotEmpty()) {
                    // Show the existing profile picture from Supabase
                    Image(
                        painter = rememberAsyncImagePainter(model = user.profile_picture),
                        contentDescription = "Existing Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder Icon
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add Profile Picture",
                        tint = Color.Black,
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                    )
                }
            }
        }

































                Spacer(modifier = Modifier.height(16.dp))

        // Editable fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
/*
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "HIDE" else "SHOW", color = Color.Black)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
*/
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address ?: "",
            onValueChange = { address = it },
            label = { Text("Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone ?: "",
            onValueChange = { phone = it },
            label = { Text("Phone number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save button
        Button(
            onClick = {
                isLoading = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            if (isLoading) {
                // Appeler LaunchedEffect ici
                LaunchedEffect(Unit) {
                    try {
                        val userRepository: UserRepository = UserRepositoryImpl()

                        println("Selected Imagee URI: ${selectedImageUri.value.toString()}")

                        userRepository.updateUser(user.id_user,  user.copy(name = name, email = email, password = password, adress = address, num_tel = phone ,profile_picture = selectedImageUri.value.toString()))


                        navController.navigate("Profil") {
                            popUpTo("Profil")
                        }

                        showSnackbar = true
                    } catch (e: Exception) {
                        // Log error or gérer un état d'erreur ici
                    } finally {
                        isLoading = false
                    }
                }
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Save", color = Color.White, fontSize = 16.sp)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

                if (showSnackbar) {
                    Snackbar(
                        action = {
                            TextButton(onClick = { showSnackbar = false }) {
                                Text("Dismiss")
                            }
                        }
                    ) {
                        Text("User updated successfully!")
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    )
}

