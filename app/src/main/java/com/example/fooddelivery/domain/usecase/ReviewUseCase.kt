package com.example.fooddelivery.domain.usecase

import com.example.fooddelivery.data.model.Review
import com.example.fooddelivery.domain.respository.ReviewRespository


class GetReviewUseCase(private val repository: ReviewRespository) {
    suspend operator fun invoke(restaurantId: String):   List<Pair<Review, String>> ?  {
        return repository.getReviewByIdrestaurant(restaurantId)
    }
}