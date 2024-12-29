import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant

class GetRestoUsecase(private val repository: restoRepository) {
    suspend operator fun invoke(): List<Restaurant> {
        return repository.getResto()
    }
}

class GetRestaurantUseCase(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke(restaurantId: String): Restaurant? {
        return repository.getRestaurantById(restaurantId)
    }
}

class GetMenuItemsUseCase(
    private val menuRepository: MenuRepository
) {

    suspend operator fun invoke(restaurantId: String, itemType: String): List<Item> {
        return menuRepository.getMenuItemsByRestaurantId(restaurantId)
    }
}
