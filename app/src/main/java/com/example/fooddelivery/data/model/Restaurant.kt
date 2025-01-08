package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id_restaurant: String,
    val name: String,
    val delivery_price: String,
    val logo: String,
    val location: String,
    val rating: Double,
    val nbr_reviews: Int,
    val phone: String,
    val email: String,
    val instagramme: String,
    val facebook: String,
    val delivery_time: String,
    val lat: Float,
    val alongitude:Float,
    val category: String,
    val delivery_man: String,
    val num_delivery_man: String

)