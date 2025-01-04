package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.item
import fetchitembyid
import getname_it
import getprice_it

interface ItemRespository {
    suspend fun getItemById(itemId: String): item?
    suspend fun getitemname(itemId: String):String?
    suspend fun getitemprice(itemId: String):Double?
}

class ItemRespositoryImpl : ItemRespository {
    override suspend fun getItemById(itemId: String): item? {
        return fetchitembyid(itemId)
    }
    override suspend fun getitemname(itemId: String): String? {
        return getname_it(itemId)
    }
override suspend fun getitemprice(itemId: String): Double? {
    return getprice_it(itemId)
}
}
