package com.example.fooddelivery.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fooddelivery.components.BackArrowButton
import com.example.fooddelivery.components.GoogleLoginIn
import com.example.fooddelivery.components.HaveAccount
import com.example.fooddelivery.components.MyTextField
import com.example.fooddelivery.components.OrSeparator
import com.example.fooddelivery.components.TitleTexte
import com.example.fooddelivery.components.passwordTextField
import com.example.fooddelivery.data.model.UserViewModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supabaseClient


@Composable
fun SignUp2 (navController: NavController){

    // to acces the infos whenver i'm
    val userViewModel: UserViewModel = viewModel()


    // State variables for input fields
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()// to vocer the whole screen

    ){

        Column (

            modifier = Modifier
                .fillMaxSize() // Makes the Column fill the entire screen
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            //verticalArrangement = Arrangement.Center // Center vertically
        ){
            BackArrowButton(navController)
            Spacer(modifier = Modifier.height(20.dp))


            TitleTexte("Sign Up")
            Spacer(modifier = Modifier.height(20.dp))

            MyTextField("Email address", value = email.value, onValueChange = { email.value = it })
            passwordTextField("Password", value = password.value, onValueChange = { password.value = it })
            Spacer(modifier = Modifier.height(12.dp))
            passwordTextField("Confirm password", value = confirmPassword.value, onValueChange = { confirmPassword.value = it })


            Spacer(modifier = Modifier.weight(1f))

            // Display error message if any
            if (errorMessage.value.isNotEmpty()) {
                Text(errorMessage.value, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }


         /*   Button(onClick = {
                // Collect email and password from the input fields
                val email = email.value
                val password = password.value
                val confirmPassword = confirmPassword.value

                // Validate fields
                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    errorMessage.value = "All fields are required"
                    return@Button
                }

                // Check if the passwords match
                if (password != confirmPassword) {
                    errorMessage.value = "Passwords do not match"
                    return@Button
                }

                // Check if the email already exists before proceeding with sign up
                composableScope.launch(Dispatchers.IO) {
                    try {
                        // Check if the email exists in Supabase (by attempting to sign in)
                        val existingUser = supabaseClient.auth.signInWith(Email)  {
                            this.email = email
                        }

                        // If the user exists, show an error message
                        withContext(Dispatchers.Main) {
                            errorMessage.value = "This email is already registered."
                        }

                    } catch (e: Exception) {
                        // If signInWithEmail fails (email doesn't exist), proceed to sign up
                        try {
                            // Proceed with the sign-up logic
                            val result = supabaseClient.auth.signUpWith(Email) {
                                this.email = email
                                this.password = password
                            }

                            // After successful sign-up, navigate to OTP page
                            withContext(Dispatchers.Main) {
                                navController.navigate("SignUp3OTP?email=${email}")
                            }

                        } catch (e: Exception) {
                            // Handle other errors (e.g., sign-up failure)
                            withContext(Dispatchers.Main) {
                                errorMessage.value = e.message ?: "Sign-up failed"
                            }
                        }
                    }
                }

            }, modifier = Modifier.fillMaxWidth().heightIn(56.dp), contentPadding = PaddingValues(), colors = ButtonDefaults.buttonColors(Color.Black), shape = RoundedCornerShape(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth().heightIn(48.dp).background(color = MainBlack), contentAlignment = Alignment.Center) {
                    Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }*/

            val isLoading = remember { mutableStateOf(false) }
            val emailExists = remember { mutableStateOf(false) }
            Button(
                onClick = {
                    val email = email.value
                    val password = password.value
                    val confirmPassword = confirmPassword.value

                    // Validation des champs
                    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        errorMessage.value = "All fields are required"
                        return@Button
                    }

                    if (password != confirmPassword) {
                        errorMessage.value = "Passwords do not match"
                        return@Button
                    }

                    // Vérifiez si l'email existe
                    composableScope.launch(Dispatchers.IO) {
                        try {
                            isLoading.value = true  // Active l'état de chargement



                            // Fetch users from Supabase
                            val fetchedUsers = supabaseClient
                                .from("user1")
                                .select(columns = Columns.list("email")) // Fetch only the email column

                            Log.d("DataType", "FetchedUsers.data type: ${fetchedUsers.data}")
                            Log.d("DataTypeCheck", "Type of fetchedUsers.data: ${fetchedUsers.data?.javaClass?.name}")

                            if (fetchedUsers.data is String) {
                                // Parse the JSON string into a list of maps
                                val gson = Gson()
                                val userListType = object : TypeToken<List<Map<String, String>>>() {}.type
                                val users: List<Map<String, String>> = gson.fromJson(fetchedUsers.data, userListType)

                                // Now check if any email matches the entered email
                                val emails = users.mapNotNull { user ->
                                    val userEmail = user["email"]?.trim()?.lowercase() // Extract and normalize email
                                    Log.d("ExtractedEmail", "Extracted email: $email") // Log extracted email
                                    userEmail // Return the email or null if not found
                                }

                                Log.d("FetchedEmails", "Emails: $emails")

                                // Now check for existence
                                emailExists.value = emails.any { it == email.lowercase() }

                                if (emailExists.value) {
                                    Log.d("EmailCheck", "Email already exists: $email")
                                } else {
                                    Log.d("EmailCheck", "Email does not exist: $email")
                                }
                            } else {
                                Log.e("DataError", "Fetched data is not a string.")
                            }


                            withContext(Dispatchers.Main) {
                                if (emailExists.value) {
                                    errorMessage.value =
 "This email is already registered."
                                } else {
                                    // Procédez à l'inscription
                                    try {
                                        val signUpResult = supabaseClient.auth.signUpWith(Email) {
                                            this.email = email
                                            this.password = password
                                        }

                                        withContext(Dispatchers.Main) {
                                            navController.navigate("SignUp3OTP?email=${email}")
                                        }
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            errorMessage.value = e.message ?: "Sign-up failed"
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                errorMessage.value = "Failed to check email existence: ${e.message}"
                            }
                        } finally {
                            isLoading.value = false  // Désactive l'état de chargement
                        }
                    }
                },
                enabled = !isLoading.value, // Désactive le bouton pendant le chargement
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(56.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp)
                        .background(color = Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Next",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }






            OrSeparator()

            GoogleLoginIn{}


            Spacer(modifier = Modifier.height(15.dp))
            HaveAccount(navController)


        }
    }
}

