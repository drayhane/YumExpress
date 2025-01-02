package com.example.fooddelivery.domain.respository

import addtopanier
import android.util.Log
import com.example.fooddelivery.data.model.compose
import modifpanier

interface ComposeRepository {
    suspend fun additemtocart(item:compose,item_price:Double)

}
class ComposeRepositoryImpl : ComposeRepository{
    override suspend fun additemtocart(item:compose,item_price:Double) {
        Log.d("composerepository", "appel to addtopanier ")

        val  cmp = addtopanier(item)
        Log.d("composerepository", "addtopanier compose est  $cmp ")

        val quantityInt = item.quantity?.toIntOrNull() ?: 0 // Si la conversion échoue, on prend 0 par défaut
        val prix = quantityInt * item_price
        Log.d("composerepository", "prix est  $prix ")

        modifpanier(item.id_card,prix)

    }
}