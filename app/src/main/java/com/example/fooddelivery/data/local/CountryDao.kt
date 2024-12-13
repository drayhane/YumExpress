import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): List<Country> // Use List<Country> instead of Object
}

