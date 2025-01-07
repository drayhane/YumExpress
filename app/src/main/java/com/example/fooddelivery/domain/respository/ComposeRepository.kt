package com.example.fooddelivery.domain.respository

import addtopanier
import android.util.Log
import com.example.fooddelivery.data.model.compose
import deletecompose
import getcardproduct
import modifpanier
import quantite_modif

interface ComposeRepository {
    suspend fun additemtocart(item:compose,item_price:Double)
    suspend fun getproducts(id_card:String):List<compose>
    suspend fun modifquantity(itemId:String,cardid:String,quantite: String,totalprice:Double)
    suspend fun deletefrompanier(itemid: String,cartid: String,totalprice: Double)
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
    //retourne la liste des produits d un panier
    override suspend fun getproducts(idcard:String):List<compose>{
        return getcardproduct(idcard)
    }
    override suspend fun modifquantity(itemId:String,cardid:String,quantite:String,totalprice:Double){
        quantite_modif(itemId,cardid,quantite,totalprice)
    }
    override  suspend fun deletefrompanier(itemid: String,cartid: String,totalprice: Double){
        deletecompose(itemid,cartid,totalprice)
    }

}