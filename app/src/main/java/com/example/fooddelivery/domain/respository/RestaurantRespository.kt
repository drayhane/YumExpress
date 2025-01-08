package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.Restaurant
import deliveryprice
import fetchrestaubyid

interface RestaurantRepository {
    suspend fun getRestaurantById(restid:String): Restaurant?
    suspend fun getdeliveryprice(restid: String):String?
}
class RestaurantRepositoryImpl : RestaurantRepository{
    override suspend fun getRestaurantById(restid:String): Restaurant? {
        return fetchrestaubyid(restid)
    }
    override suspend fun getdeliveryprice(restid: String): String? {
        return deliveryprice(restid)
    }
}