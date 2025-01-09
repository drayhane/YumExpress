package com.example.fooddelivery.data.local

import androidx.room.Dao
import androidx.room.Query


@Dao
interface OrderDao {
    @Query("SELECT * FROM `Order` WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): OrderEntity
}