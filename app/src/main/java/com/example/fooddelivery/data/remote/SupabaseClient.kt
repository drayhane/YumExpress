
import com.example.fooddelivery.data.model.User1
import com.example.fooddelivery.data.model.item
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns


val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kfhcvlegzuemrxwfkgak.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtmaGN2bGVnenVlbXJ4d2ZrZ2FrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM5NTQxOTMsImV4cCI6MjA0OTUzMDE5M30.oizzttRgeJEtvcozA5fkCbmO8fynjmd4EgGNBCzYGMA"
) {
    install(Postgrest)
}
suspend fun fetchUserById(userid: String): User1? {
    val response = supabaseClient.from("user1").select(columns = Columns.list("*")) {
        filter {
            eq("id_user", userid)
        }
    }
    // Decode the data into a Restaurant object
    return response.decodeSingleOrNull<User1>()
}
suspend fun fetchUpdateUser(userId: String, updatedUser: User1): User1? {

        val response = supabaseClient.from("user1").update(
            {
                set("email" , updatedUser.email)
                set("password" , updatedUser.password)
                set("adress" , updatedUser.adress)
                set("num_tel" , updatedUser.num_tel)
                set("name", updatedUser.name)
            }
        ) {
            select()
            filter {

                eq("id_user", userId)
            }
        }

    return response.decodeSingleOrNull<User1>()
}
suspend fun fetchitembyid(itemid:String):item?{
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_item", itemid)
        }
    }
    // Decode the data into a Restaurant object
    return response.decodeSingleOrNull<item>()
}
