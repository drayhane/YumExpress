import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.PostgrestFilterBuilder


val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kfhcvlegzuemrxwfkgak.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtmaGN2bGVnenVlbXJ4d2ZrZ2FrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM5NTQxOTMsImV4cCI6MjA0OTUzMDE5M30.oizzttRgeJEtvcozA5fkCbmO8fynjmd4EgGNBCzYGMA"
) {
    install(Postgrest)
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

suspend fun fetchMenuItems(restaurantId: String, Type: String): List<Item> {
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_restaurant", restaurantId)
            eq("Type", Type)
        }
    }
    // Decode the data into a Restaurant object
    return response.decodeList<Item>()
}
suspend fun fetchcatego(restaurantId: String): List<String> {
    val response = supabaseClient.from("item").select(columns = Columns.list("Type")) {
        filter {
            eq("id_restaurant", restaurantId)
        }
    }
    return response.decodeList<String>()
}
