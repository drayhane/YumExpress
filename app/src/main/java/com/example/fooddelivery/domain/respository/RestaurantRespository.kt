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
}

class RestaurantRepositoryImpl : RestaurantRepository {

    override suspend fun getRestaurantById(restaurantId: String): Restaurant? {
        return fetchRestaurantById(restaurantId)
    }

}

interface MenuRepository {
    suspend fun getCategories(restaurantId: String): List<String>
    suspend fun getMenuItemsByRestaurantId(restaurantId: String, Type: String): List<Item>
}

class MenuRepositoryImpl : MenuRepository {
    override suspend fun getMenuItemsByRestaurantId(restaurantId: String, Type: String): List<Item> {
        return fetchMenuItems(restaurantId, Type)
    }

    override suspend fun getCategories(restaurantId: String): List<String> {
        return fetchcatego(restaurantId)
    }
}
