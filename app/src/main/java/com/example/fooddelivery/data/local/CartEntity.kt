import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_card") val idCard: String,
    @ColumnInfo(name = "total_price") val totalPrice: Double?,
    @ColumnInfo(name = "food_note") val foodNote: String?,
    @ColumnInfo(name = "id_user") val userId: String, // clé étrangère vers user1
    @ColumnInfo(name = "items") val items: List<ComposeEntity> // relation avec ComposeEntity
)
