package com.example.fooddelivery.domain.respository


import com.example.fooddelivery.data.model.Restaurant
import fetchRestaurant


interface restoRepository {
    suspend fun getResto(): List<Restaurant>
    suspend fun syncResto(remoteR: List<Restaurant>)
}

class restoRepositoryImpl : restoRepository {

    override suspend fun getResto(): List<Restaurant> {
        return fetchRestaurant()
    }

    override suspend fun syncResto(remoteR: List<Restaurant>) {
    }
}