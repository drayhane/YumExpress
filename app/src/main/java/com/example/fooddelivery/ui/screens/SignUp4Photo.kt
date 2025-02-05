package com.example.fooddelivery.ui.screens

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.HaveAccount
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.data.local.Cart
import com.example.fooddelivery.data.local.User1
import com.example.fooddelivery.data.model.UserViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import supabaseClient
import java.util.UUID


fun showPictureOptions(
    context: Context,
    launcherGallery: ManagedActivityResultLauncher<String, Uri?>,
    launcherCamera: ManagedActivityResultLauncher<Void?, Bitmap?>
) {
    val options = arrayOf("Choose from Gallery", "Take a Picture")
    AlertDialog.Builder(context)
        .setTitle("Select Option")
        .setItems(options) { _, which ->
            when (which) {
                0 -> launcherGallery.launch("image/*") // Gallery
                1 -> launcherCamera.launch(null) // Camera
            }
        }
        .setNegativeButton("Cancel", null)
        .show()
}

fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val contentResolver = context.contentResolver
    val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val imageDetails = ContentValues().apply {
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "profile_picture_${System.currentTimeMillis()}.jpg"
        )
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
    }

    val imageUri = contentResolver.insert(imageCollection, imageDetails)

    try {
        imageUri?.let { uri ->
            val outputStream = contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            outputStream?.close()
            return uri
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

@Composable
fun SignUp4Photo(navController: NavController) {

    // to access users informations
    val userViewModel: UserViewModel = viewModel()

    val users = remember { mutableStateListOf<User1>() }
    var newUser by remember { mutableStateOf("") }

    // State to hold the selected image URI
    var name = remember { mutableStateOf("") }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val phoneNumber = remember { mutableStateOf("") }
    val userAddress = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    // Context and launchers for gallery and camera
    val context = LocalContext.current
    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri.value = uri // Save the selected image URI
    }

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val imageUri = saveBitmapToUri(context, bitmap)
            if (imageUri != null) {
                selectedImageUri.value = imageUri
                Toast.makeText(context, "Picture Taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BackArrowButton(navController)
        Spacer(modifier = Modifier.height(20.dp))

        TitleTexte("Sign Up")
        Spacer(modifier = Modifier.height(40.dp))
        // Circular profile picture display
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(140.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
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
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
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

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Add a Profile Picture",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyTextField("Name", value = name.value, onValueChange = { name.value = it })
        MyTextField(
            "Phone number",
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it })
        MyTextField(
            "Address",
            value = userAddress.value,
            onValueChange = { userAddress.value = it })

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                composableScope.launch(Dispatchers.IO) {
                    //  try {
                    // Get the current user's ID from Supabase
                    val currentUser = supabaseClient.auth.currentUserOrNull()
                    val userId = currentUser?.id ?: throw Exception("User not authenticated")
                    val email = currentUser.email
                    val password = "no need"

                    // to add the cart
                    val uniqueCardId = UUID.randomUUID().toString()

                    val cart = Cart(id_card = uniqueCardId, total_price = 0.0,food_note="0", Id_rest = null, is_active= true)
                    supabaseClient.from("cart").insert(cart)

                    // Prepare data for insertion
                    val dataToInsert = mapOf(
                        "id_user" to userId,
                        "email" to email,
                        "password" to password,
                        "profile_picture" to selectedImageUri.value?.toString(),
                        "num_tel" to phoneNumber.value,
                        "adress" to userAddress.value,
                        "location" to "null",
                        "name" to name.value,
                        "id_card" to uniqueCardId
                    )

                    // Insert into user1 table
                    val user = supabaseClient.from("user1").insert(dataToInsert) {
                        select()
                        single()
                    }.decodeAs<User1>()
                    users.add(user)
                    newUser = ""

                    // Handle success (e.g., navigate or show a success message)
                    println("User profile saved successfully: $user")
                    /*  } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Failed to save profile"
                        }
                    }*/

                    // Switch to Main thread for navigation
                    withContext(Dispatchers.Main) {
                        navController.navigate("SignUpSuccess")
                    }
                }
            },modifier = Modifier
                .fillMaxWidth()
                .heightIn(56.dp),

            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(Color.Black),
            shape = RoundedCornerShape(8.dp)

        ) {
            Box (modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    color = Color.Black,
                ),
                contentAlignment = Alignment.Center)
            {
                Text("Sign up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

            Spacer(modifier = Modifier.height(15.dp))
            HaveAccount(navController)
        }
    }


