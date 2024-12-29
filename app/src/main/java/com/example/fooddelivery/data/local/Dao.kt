import androidx.room.Dao
import androidx.room.Query
import com.example.fooddelivery.data.model.Restaurant

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): List<Country> // Use List<Country> instead of Object
}
@Dao
interface RestaurantDao{
    @Query("SELECT * FROM Restaurant WHERE id_restaurant = :id_restaurant")
    fun getrestaurant(id_restaurant: String): Restaurant
}

