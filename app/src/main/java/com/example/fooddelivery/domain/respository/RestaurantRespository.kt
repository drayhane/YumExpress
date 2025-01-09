
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant


interface restoRepository {
    suspend fun getResto(): List<Restaurant>
    suspend fun syncResto(remoteR: List<Restaurant>)
}

class restoRepositoryImpl : restoRepository {

    override suspend fun getResto(): List<Restaurant> {
        return fetchRestaurant()
    }

    override suspend fun syncResto(remoteR: List<Restaurant>) {
    }
}

interface RestaurantRepository {
    suspend fun getRestaurantById(restaurantId: String): Restaurant?
    suspend fun getdeliveryprice(restid: String):String?

}

class RestaurantRepositoryImpl : RestaurantRepository {

    override suspend fun getRestaurantById(restaurantId: String): Restaurant? {
        return fetchRestaurantById(restaurantId)
    }
    override suspend fun getdeliveryprice(restid: String): String? {
        return deliveryprice(restid)
    }

}

interface MenuRepository {
    suspend fun getMenuItemsByRestaurantId(restaurantId: String): List<Item>
}

class MenuRepositoryImpl : MenuRepository {
    override suspend fun getMenuItemsByRestaurantId(restaurantId: String): List<Item> {
        return fetchMenuItems(restaurantId)
    }
}

interface reviewRespository {
    suspend fun addReview(restaurantId: String, userId: String, rating: Int, reviewText:String): Boolean
}

class reviewRespositoryImpl : reviewRespository {
    override suspend fun addReview(restaurantId: String, userId: String, rating: Int, reviewText:String): Boolean {
        return AddReview(restaurantId, userId, rating, reviewText)
    }
}


