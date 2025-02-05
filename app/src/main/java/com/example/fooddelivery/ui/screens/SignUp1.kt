package com.example.fooddelivery.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.example.fooddelivery.R
import com.example.fooddelivery.components.ControllerText
import com.example.fooddelivery.components.DontHaveAccount
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.OrSeparator
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField
import com.example.fooddelivery.data.local.Cart
import com.example.fooddelivery.data.local.User1
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supabaseClient
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID


@Composable
fun SignUp1 (navController: NavController){

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }
    val composableScope = rememberCoroutineScope()

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()

    ){

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo background",
                modifier = Modifier
                    .width(290.dp)
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            TitleTexte("Log in")
            Spacer(modifier = Modifier.height(20.dp))
            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
            Spacer(modifier = Modifier.height(4.dp))
            ControllerText("Forgot Password ?",navController,"ForgotPassword1")


            Spacer(modifier = Modifier.height(90.dp))
            Spacer(modifier = Modifier.weight(1f))


            Button(onClick = {

                if (email.value.isBlank() || password.value.isBlank()) {
                    errorMessage.value = "All fields are required"
                    return@Button
                }

                // Call Supabase to log in
                composableScope.launch(Dispatchers.IO) {
                    try {
                        // Use Supabase Auth to sign in with Email
                        val result = supabaseClient.auth.signInWith(Email) {
                            this.email = email.value.trim()
                            this.password = password.value.trim()
                        }


                        // After successful login, save login status
                        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()

                        // Switch to the Main thread to handle navigation
                        withContext(Dispatchers.Main) {
                            navController.navigate("HomeScreen") // Navigate to the Home screen or appropriate page
                        }

                    } catch (e: Exception) {
                        // Handle login errors
                        withContext(Dispatchers.Main) {
                            errorMessage.value = e.message ?: "Login failed. Please try again."
                        }
                    }
                }
            },
                modifier = Modifier
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
                    Text(text = "Log in")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OrSeparator()
            GoogleButton(navController)

            Spacer(modifier = Modifier.height(15.dp))

            DontHaveAccount(navController)
        }
    }

}




@Composable
fun GoogleButton(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    val emailExists = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val users = remember { mutableStateListOf<User1>() }
    var newUser by remember { mutableStateOf("") }
    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        // Generate a nonce and hash it with sha-256
        // Providing a nonce is optional but recommended
        val rawNonce = UUID.randomUUID().toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
        val bytes = rawNonce.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in




        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("892291624682-oltjcnjapqak09e86nv142t8auivcbjo.apps.googleusercontent.com")
            .setNonce(hashedNonce) // Provide the nonce if you have one
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(result.credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                supabaseClient.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }


                val currentUser = supabaseClient.auth.currentUserOrNull()

                if (currentUser != null) {


                    // to check if user is already registered or no
                    // Fetch users from Supabase
                    val fetchedUsers = supabaseClient
                        .from("user1")
                        .select(columns = Columns.list("email")) // Fetch only the email column

                    if (fetchedUsers.data is String) {
                        // Parse the JSON string into a list of maps
                        val gson = Gson()
                        val userListType = object : TypeToken<List<Map<String, String>>>() {}.type
                        val users: List<Map<String, String>> =
                            gson.fromJson(fetchedUsers.data, userListType)

                        // Now check if any email matches the entered email
                        val emails = users.mapNotNull { user ->
                            val userEmail =
                                user["email"]?.trim()?.lowercase() // Extract and normalize email
                            Log.d(
                                "ExtractedEmail",
                                "Extracted email: ${currentUser.email}"
                            ) // Log extracted email
                            userEmail // Return the email or null if not found
                        }

                        Log.d("FetchedEmails", "Emails: $emails")

                        // Now check for existence
                        emailExists.value = emails.any { it == currentUser.email?.lowercase() }

                    }

                    // if the user already exsist just log in , no need to register him again
                    if (!emailExists.value) {
                    val uniqueCardId = UUID.randomUUID().toString()

                    val cart = Cart(
                        id_card = uniqueCardId,
                        total_price = 0.0,
                        food_note = "0",
                        Id_rest = null,
                        is_active = true
                    )
                    supabaseClient.from("cart").insert(cart)

                    // Prepare data for insertion
                    val dataToInsert = mapOf(
                        "id_user" to currentUser.id,
                        "email" to currentUser.email,
                        "password" to "no need", // Assuming you don't need password
                        "profile_picture" to null, // Assuming profile picture is not available here
                        "num_tel" to "", // Assuming no phone number is available here
                        "adress" to "", // Assuming no address is available here
                        "location" to "null",
                        "name" to googleIdTokenCredential.displayName,
                        "id_card" to uniqueCardId
                    )
                    // Insert into user1 table
                    val user = supabaseClient.from("user1").insert(dataToInsert) {
                        select()
                        single()
                    }.decodeAs<User1>()
                    users.add(user)
                    newUser = ""
                }
            }
                sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                navController.navigate("HomeScreen") {
                    popUpTo("LogIn") { inclusive = true }
                }

                // Handle successful sign-in
            } catch (e: GetCredentialException) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            } catch (e: GoogleIdTokenParsingException) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            } catch (e: RestException) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Button Style
    Spacer(modifier = Modifier.height(12.dp))

    // Row layout for the button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp)) // Rounded corners
            .background(Color.White) // White background
            .clickable(onClick = onClick) // Handle click
            .border(1.dp, Color.LightGray, RoundedCornerShape(32.dp)), // Light gray border
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Google logo
        Image(
            painter = painterResource(id = R.drawable.googlelogo),
            contentDescription = "Google Logo",
            modifier = Modifier
                .height(24.dp)
                .padding(end = 8.dp) // Space between logo and text
        )

        // Text
        Text(
            text = "Continue with Google",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
}


/*
@Composable
fun GoogleButton(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val users = remember { mutableStateListOf<User1>() }
    var newUser by remember { mutableStateOf("") }
    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        // Generate a nonce and hash it with sha-256
        // Providing a nonce is optional but recommended
        val rawNonce = UUID.randomUUID().toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
        val bytes = rawNonce.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in




        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("892291624682-oltjcnjapqak09e86nv142t8auivcbjo.apps.googleusercontent.com")
            .setNonce(hashedNonce) // Provide the nonce if you have one
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(result.credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                supabaseClient.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }


                val currentUser = supabaseClient.auth.currentUserOrNull()

                if (currentUser != null) {




                    val uniqueCardId = UUID.randomUUID().toString()

                    val cart = Cart(id_card = uniqueCardId, total_price = 0.0,food_note="0",Id_rest=null, is_active= false)
                    supabaseClient.from("cart").insert(cart)

                    // Prepare data for insertion
                    val dataToInsert = mapOf(
                        "id_user" to currentUser.id,
                        "email" to currentUser.email,
                        "password" to "no need", // Assuming you don't need password
                        "profile_picture" to null, // Assuming profile picture is not available here
                        "num_tel" to "", // Assuming no phone number is available here
                        "adress" to "", // Assuming no address is available here
                        "location" to "null",
                        "name" to googleIdTokenCredential.displayName,
                        "id_card" to uniqueCardId
                    )
                    // Insert into user1 table
                    val user = supabaseClient.from("user1").insert(dataToInsert) {
                        select()
                        single()
                    }.decodeAs<User1>()
                    users.add(user)
                    newUser = ""

                }

                    navController.navigate("HomeScreen")
                // Handle successful sign-in
            } catch (e: GetCredentialException) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            } catch (e: GoogleIdTokenParsingException) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            } catch (e: RestException) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Button Style
    Spacer(modifier = Modifier.height(12.dp))

    // Row layout for the button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp)) // Rounded corners
            .background(Color.White) // White background
            .clickable(onClick = onClick) // Handle click
            .border(1.dp, Color.LightGray, RoundedCornerShape(32.dp)), // Light gray border
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Google logo
        Image(
            painter = painterResource(id = R.drawable.googlelogo),
            contentDescription = "Google Logo",
            modifier = Modifier
                .height(24.dp)
                .padding(end = 8.dp) // Space between logo and text
        )

        // Text
        Text(
            text = "Continue with Google",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
}
*/




fun isUserLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("is_logged_in", false)
}
