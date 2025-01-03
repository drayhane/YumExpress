package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.Item
import fetchitembyid

interface ItemRespository {
    suspend fun getItemById(itemId: String): Item?

}

class ItemRespositoryImpl : ItemRespository {
    override suspend fun getItemById(itemId: String): Item? {
        return fetchitembyid(itemId)
    }

}
