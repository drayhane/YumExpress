import android.database.Cursor
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.Review
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}




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


class RestaurantRepositoryImpl (
    private val restaurantDao: RestaurantDao, // Local database DAO
    private val context: Context


) : RestaurantRepository {


    override suspend fun getRestaurantById(restaurantId: String): Restaurant? {
        return if (isInternetAvailable(context)) {
            // Fetch data from the remote server
            val remoteRestaurant = fetchRestaurantById(restaurantId)
            if (remoteRestaurant != null) {
                // Save the fetched data to the local database for offline access
                restaurantDao.insertRestaurant(remoteRestaurant)
            }
            remoteRestaurant
        } else {
            // Fetch data from the local database when offline
            restaurantDao.getRestaurantById(restaurantId)
        }

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



