import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "compose",
    primaryKeys = ["id_item", "id_card"],
    foreignKeys = [
        ForeignKey(entity = CartEntity::class, parentColumns = ["id_card"], childColumns = ["id_card"]),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id_item"], childColumns = ["id_item"])
    ]
)
data class ComposeEntity(
    @ColumnInfo(name = "id_item") val idItem: ItemEntity, // Utilisation de ItemEntity ici
    @ColumnInfo(name = "id_card") val idCard: String,
    @ColumnInfo(name = "quantity") val quantity: String?,
    @ColumnInfo(name = "supp") val supp: String?,
    @ColumnInfo(name = "sauce") val sauce: String?,
    @ColumnInfo(name = "size") val size: String?
)
