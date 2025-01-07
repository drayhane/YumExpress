package com.example.fooddelivery.domain.respository

import FetchUserOrders
import com.example.fooddelivery.data.model.order1
import create_order

interface OrderRespository {
    suspend fun neworder(Order:order1):Boolean
    suspend fun Getorders(userId: String):List<order1>
}
class OrderRespositoryImpl : OrderRespository{
    override suspend fun neworder(Order:order1):Boolean
    {
        return create_order(Order)
    }
    override suspend fun Getorders(userId: String):List<order1>{
        return FetchUserOrders(userId)
    }
}