package com.example.fooddelivery.data.model
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id_restaurant: String,
    val id_user: String,
    val review: String,
    val date: String,
    val note: Int,
)