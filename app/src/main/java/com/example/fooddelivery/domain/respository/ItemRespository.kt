package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.item
import fetchitembyid

interface ItemRespository {
    suspend fun getItemById(itemId: String): item?

}

class ItemRespositoryImpl : ItemRespository {
    override suspend fun getItemById(itemId: String): item? {
        return fetchitembyid(itemId)
    }

}
