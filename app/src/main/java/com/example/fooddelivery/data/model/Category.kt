package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val name: String?,
    val image: String
)