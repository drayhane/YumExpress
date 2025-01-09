package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.Review
import fetchReviewsFromSupabase

interface ReviewRespository{
    suspend fun getReviewByIdrestaurant(restaurantId: String): List<Pair<Review, String>> ?
}

class ReviewRespositoryImpl: ReviewRespository {

    override suspend fun getReviewByIdrestaurant(restaurantId: String): List<Pair<Review, String>> {
        // Fetch the reviews from Supabase or wherever the data is coming from
        return fetchReviewsFromSupabase(restaurantId)
    }
}
