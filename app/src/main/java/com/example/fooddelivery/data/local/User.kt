package com.example.fooddelivery.data.local

import kotlinx.serialization.Serializable



@Serializable
data class User1(
    val id_user: String,
    val email: String,
    val password: String,
    val profile_picture: String?,
    val num_tel: String,
    val adress: String,
    val location: String?,
    val name: String,
    val id_card: String?
)