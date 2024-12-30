package com.example.fooddelivery.data.model
import kotlinx.serialization.Serializable
import java.sql.Date


@Serializable
data class Review(
    val id_restaurant: String,
    val id_user: String,
    val date: String,
    val note:  Int?,
    val review: String?

)