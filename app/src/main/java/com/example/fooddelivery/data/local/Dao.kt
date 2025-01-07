import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fooddelivery.data.model.Restaurant
import kotlinx.coroutines.flow.Flow


@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant WHERE id_restaurant = :restaurantId")
    fun getRestaurantById(restaurantId: String): Restaurant?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: Restaurant)
}
