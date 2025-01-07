import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fooddelivery.data.model.Restaurant


@Database(entities = [Restaurant::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}
