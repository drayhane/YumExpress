import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CountryEntity::class, RestaurantEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
}
