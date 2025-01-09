package com.example.fooddelivery.domain.respository


import addtofavorit
import com.example.fooddelivery.data.model.Restaurant
import deletefavoris
import getFavoriteRestaurants
import removeFromFavorites


interface FavorisRepository {
    suspend fun getFavRestaurants(userId: String): List<Restaurant>?
    suspend fun deletefav(idUser: String, idRestaurant : String)
    suspend fun removeFromFav(userId: String , idRestaurant: String)
    suspend fun addfavorits (idUser: String , idRestaurant: String)

}

class FavorisRepositoryImpl : FavorisRepository {

    override suspend fun getFavRestaurants(userId: String): List<Restaurant>? {
        return getFavoriteRestaurants(userId)
    }

    override suspend fun deletefav(idUser: String, idRestaurant : String) {
        return deletefavoris(idUser, idRestaurant )
    }

    override suspend fun removeFromFav(idUser: String , idRestaurant: String) {
        return removeFromFavorites(idUser , idRestaurant)
    }
    override suspend fun addfavorits (idUser: String , idRestaurant: String){
      addtofavorit(idUser , idRestaurant)    }


}