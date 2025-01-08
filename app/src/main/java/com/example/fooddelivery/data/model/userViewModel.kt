package com.example.fooddelivery.data.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.serialization.Serializable

class UserViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
}

