import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity (tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey val id_restaurant: String,
    val name: String,
    val delivery_price: String?,
    val logo: String?,
    val location: String,
    val type: String,
    val rating: Double,
    val nbr_reviews: Int,
    val phone: String?,
    val email: String?,
    val instagramme: String?,
    val facebook: String?,
    val delivery_time: String?,
)



