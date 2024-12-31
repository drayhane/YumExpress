import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "item")
data class ItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_item") val idItem: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "price") val price: Double?,
    @ColumnInfo(name = "id_restaurant") val idRestaurant: String?
)
