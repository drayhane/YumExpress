package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.restau
import deliveryprice
import fetchrestaubyid

interface restRepository {
    suspend fun getRestaubyid(restid:String): restau?
    suspend fun getdeliveryprice(restid: String):String?
}
class restRepositoryImpl : restRepository{
    override suspend fun getRestaubyid(restid:String): restau? {
        return fetchrestaubyid(restid)
    }
    override suspend fun getdeliveryprice(restid: String): String? {
        return deliveryprice(restid)
    }
}