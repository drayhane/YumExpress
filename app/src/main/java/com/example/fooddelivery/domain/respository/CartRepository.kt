package com.example.fooddelivery.domain.respository
import EndPanier
import com.example.fooddelivery.data.model.Cart
import createUserCart
import getActiveCart

interface CartRepository {
    suspend fun CreateCart(userId: String,restaurantId:String): Cart?
    suspend fun Getactivecart(userId: String):Cart?
   suspend fun Finirpanier(cartid: String)
}
class CartRepositoryImpl : CartRepository {

    override suspend fun CreateCart(userId: String,restaurantId:String):Cart?  {
    return createUserCart(userId, restaurantId )
    }
    override suspend fun Getactivecart(userId: String):Cart?{
        return getActiveCart(userId)
    }
    override suspend fun Finirpanier(cartid: String){
        //mettre is active a false
        EndPanier(cartid)

    }

    }