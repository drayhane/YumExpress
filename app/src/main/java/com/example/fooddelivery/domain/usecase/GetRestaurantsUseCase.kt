import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import kotlinx.datetime.LocalDate

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

class AddReviewUseCase(private val ReviewsRepository: reviewRespository) {

    suspend operator fun invoke(restaurantId: String, userId: String, rating: Int,reviewText:String): Boolean {
        if (rating !in 0..5) throw IllegalArgumentException("Rating must be between 1 and 5")
        return ReviewsRepository.addReview(restaurantId, userId,rating,reviewText)
    }
}

