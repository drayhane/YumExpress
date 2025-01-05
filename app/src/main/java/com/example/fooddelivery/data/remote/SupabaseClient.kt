import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fooddelivery.data.model.Category
import com.example.fooddelivery.data.model.Restaurant
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder
import kotlinx.datetime.LocalDate

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kfhcvlegzuemrxwfkgak.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtmaGN2bGVnenVlbXJ4d2ZrZ2FrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM5NTQxOTMsImV4cCI6MjA0OTUzMDE5M30.oizzttRgeJEtvcozA5fkCbmO8fynjmd4EgGNBCzYGMA"
) {
    install(Postgrest){

    }
}
//------------------------Restaurants----------------------------------//
suspend fun fetchRestaurant(): List<Restaurant> {
    val response = supabaseClient.postgrest["restaurant"]
        .select(Columns.ALL)
    // Décoder les données
    return response.decodeList<Restaurant>()
}
/*suspend fun fetchRestaurant(): List<Restaurant> {
    val response = supabaseClient.postgrest["restaurant"]
        .select(Columns.ALL)
    val fetchedRestaurants = response.decodeList<Restaurant>()

    // Ajouter un log pour vérifier la structure des données
    println("Fetched restaurants: $fetchedRestaurants")
    return fetchedRestaurants
}*/

//------------------------Catrgories---------------------------------//
/*suspend fun fetchCategory(): List<Category>{
    val response = supabaseClient.from("category")
        .select(columns = Columns.list("*"))
    // Décoder les données
    return response.decodeList<Category>()
}*/
//------------------------Categories----------------------------------//


suspend fun fetchCategories(): List<Category> {
    val response = supabaseClient.postgrest["category"]
        .select(Columns.ALL)
    // Décoder les données
    return response.decodeList<Category>()
}
