import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.Review
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate


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



