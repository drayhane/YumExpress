import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.Review
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder

import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kfhcvlegzuemrxwfkgak.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtmaGN2bGVnenVlbXJ4d2ZrZ2FrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM5NTQxOTMsImV4cCI6MjA0OTUzMDE5M30.oizzttRgeJEtvcozA5fkCbmO8fynjmd4EgGNBCzYGMA"

) {
    install(Postgrest){
    }
}

suspend fun fetchRestaurant(): List<Restaurant> {
    val response = supabaseClient.postgrest["restaurant"]
        .select(Columns.ALL)

    // Décodage des données
    return response.decodeList<Restaurant>()
}

suspend fun fetchRestaurantById(restaurantId: String): Restaurant? {
    val response = supabaseClient.from("restaurant").select(columns = Columns.list("*")) {
        filter {
            eq("id_restaurant", restaurantId)
        }
    }
    // Decode the data into a Restaurant object
    return response.decodeSingleOrNull<Restaurant>()
}

suspend fun fetchMenuItems(restaurantId: String): List<Item> {
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_restaurant", restaurantId)
        }
    }
    return response.decodeList<Item>()
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun AddReview(restaurantId: String, userId: String, rating: Int, reviewText: String):Boolean{
    try {
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        val new_review = Review(
            id_restaurant = restaurantId,
            id_user = userId,
            note = rating,
            review = reviewText,
            date = currentDate
        )
        val response = supabaseClient
            .from("review")
            .insert(
                new_review
            )
        // If no error, return true
        return true
    } catch (e: Exception) {
        println("Error: ${e.localizedMessage}")
        return false
    }
}




@Serializable
data class ReviewResponse(
    val id_restaurant: String,
    val id_user: String,
    val date: String,
    val note: Int?,
    val review: String?,
    val user1: UserResponse // Nested user object
)

@Serializable
data class UserResponse(
    val name: String
)

suspend fun fetchReviewsFromSupabase(restaurantId: String): List<Pair<Review, String>> {
    val response = supabaseClient
        .from("review")
        .select(columns = Columns.list("*", "user1(name)")) {
            filter {
                eq("id_restaurant", restaurantId)
            }
        }

    // Decode the response directly into a list of ReviewResponse objects
    val reviews = response.decodeList<ReviewResponse>()

    // Transform the result into a list of Pair<Review, String>
    return reviews.map { reviewData ->
        val review = decodeReview(reviewData)
        val userName = reviewData.user1.name
        Pair(review, userName)
    }
}

private fun decodeReview(data: ReviewResponse): Review {
    return Review(
        id_restaurant = data.id_restaurant,
        id_user = data.id_user,
        date = data.date,
        note = data.note,
        review = data.review
    )
}

