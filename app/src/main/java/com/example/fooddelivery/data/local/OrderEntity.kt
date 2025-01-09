package com.example.fooddelivery.data.local

import CartEntity
import androidx.room.Entity;
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Order",

)

data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val restaurantName: String,
    val totalPrice: String,
    val date: String,
    val status: String,
    val imageRes: Int, // Ressource pour l'image


    )