




import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fooddelivery.data.model.Cart
import com.example.fooddelivery.data.model.Category
import com.example.fooddelivery.data.model.FavoriteId
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.Review
import com.example.fooddelivery.data.model.User1
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.data.model.favoris_res
import com.example.fooddelivery.data.model.order1
import com.google.gson.Gson
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kfhcvlegzuemrxwfkgak.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtmaGN2bGVnenVlbXJ4d2ZrZ2FrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM5NTQxOTMsImV4cCI6MjA0OTUzMDE5M30.oizzttRgeJEtvcozA5fkCbmO8fynjmd4EgGNBCzYGMA"

) {
    install(Auth)
    install(Postgrest)
}

suspend fun updateResCard(cartid: String,restid:String){

    val response = supabaseClient.from("cart").update(
        {
            set("Id_rest" , restid)
        }
    ) {
        select()
        filter {

            eq("id_card", cartid)
        }
    }

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
    // Decode the data into a Restaurant object
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
//------------------------Restaurants----------------------------------//
suspend fun fetchRestaurant(): List<Restaurant> {
    val response = supabaseClient.postgrest["restaurant"]
        .select(Columns.ALL)
    // Décoder les données
    return response.decodeList<Restaurant>()
}


suspend fun fetchUserById(userid: String): User1? {
println("fetchuserby id")
    val response = supabaseClient.from("user1").select(columns = Columns.list("*")) {
        filter {
            eq("id_user", userid)
        }
    }
    println("terminer ")



    // Décoder la réponse en un objet User1
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
suspend fun getActiveCart(userId: String): Cart? {
    val userResponse = supabaseClient.from("user1").select(columns = Columns.list("*")) {
        filter {
            eq("id_user", userId)
        }
    }
    val idCard = userResponse.decodeSingleOrNull<User1>()?.id_card ?: return null

    // Vérifier si ce panier est actif
    val cartResponse = supabaseClient.from("cart").select(columns = Columns.list("*")) {
        filter {
            eq("id_card", idCard)
            eq("is_active", true)
        }
    }
    return cartResponse.decodeSingleOrNull<Cart>()
}

suspend fun hasActiveCartForRestaurant(userId: String, restaurantId: String): Boolean {
    // Requête pour récupérer l'id_card associé à l'utilisateur
    val userResponse = supabaseClient.from("user1").select(columns = Columns.list("id_card")) {
        filter {
            eq("id_user", userId)
        }
    }
    val idCard = userResponse.decodeSingleOrNull<User1>()?.id_card

    // Si aucun panier n'est associé à l'utilisateur, retourner false
    if (idCard == null) {
        return false
    }

    // Vérification dans la table `cart` pour un panier actif et lié au restaurant
    val cartResponse = supabaseClient.from("cart").select(columns = Columns.list("id_card")) {
        filter {
            eq("id_card", idCard)
            eq("id_restaurant", restaurantId)
            eq("is_active", true)
        }
    }
    return cartResponse.decodeSingleOrNull<Cart>() != null
}

suspend fun fetchitembyid(itemid:String):Item?{
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_item", itemid)
        }
    }
    // Decode the data into a Restaurant object
    return response.decodeSingleOrNull<Item>()
}
//creer un nouveau panier et l'attribuer a un user
@SuppressLint("SuspiciousIndentation")
suspend fun createUserCart(userId: String, restaurantId: String): Cart? {
    // Créer un nouvel objet Cart avec un UUID généré
    val cartData = mapOf(
        "total_price" to 0.0,
        "food_note" to "",
        "Id_rest" to restaurantId,
        "is_active" to true
    )
val  cart=Cart(
    total_price = 0.0,
    food_note = "",
    Id_rest = restaurantId,
    is_active = true
)
    return try {

        // Insérer le panier dans la base de données
      val response =supabaseClient
            .from("cart")
            .insert(cart)
            { select()
             }.decodeSingle<Cart>()

        // Mettre à jour la table `user1` avec l'ID du panier
        supabaseClient
            .from("user1")
            .update(mapOf("id_card" to response.id_card)) // Associe le panier à l'utilisateur
            {select()
                filter {eq("id_user", userId)  }}


        // Retourne le panier créé
        response
    } catch (e: Exception) {
        Log.e("Supabase", "Erreur lors de la création ou mise à jour : ${e.message}")
        null
    }
}


//modifier restaurantid dans panier
suspend fun ModifRestauPanier(cartid:String,restaurantId: String){
    val response = supabaseClient.from("Cart").update(
        {
            set("Id_rest" , restaurantId)

        }
    ) {
        select()
        filter {

            eq("id_card", cartid)
        }
    }
}
//mettre fin a un panier
suspend fun EndPanier(cartid: String){
    val response = supabaseClient.from("cart").update(
        {
            set("is_active" , false)

        }
    ) {
        select()
        filter {

            eq("id_card", cartid)
        }
    }
    Log.d("EndPanier", "panier=false")

}

suspend fun addtopanier(item: compose) {
    try {
        Log.d("hhhhhhhhhhhhhhhhhhh","compose=$item")
        // Récupérer l'élément existant avec la même clé primaire
        val response = supabaseClient.from("compose")
            .select(columns = Columns.list("*")) {
                filter {
                    eq("id_item", item.id_item)
                    eq("id_card", item.id_card)
                }
            }

        // Vérifier si l'élément existe
        val existingItem = response.decodeSingleOrNull<compose>()

        if (existingItem == null) {
            val dataToInsert = mapOf(
                "id_item" to item.id_item,
                "id_card" to item.id_card,
                "quantity" to item.quantity,
                "sauce" to item.sauce,
                "size" to item.size,
                "note" to item.note
            )

            val insertResponse = supabaseClient
                .from("compose")
                .insert(dataToInsert)



        } else {
            // L'élément existe, mettre à jour la quantité
            val newQuantity = (existingItem.quantity?.toIntOrNull() ?: 0) + (item.quantity?.toIntOrNull() ?: 0)

            val updateResponse = supabaseClient
                .from("compose")
                .update(
                    mapOf("quantity" to newQuantity)
                ) {
                    filter {
                        eq("id_item", item.id_item)
                        eq("id_card", item.id_card)
                    }
                }


        }
    } catch (e: Exception) {
        Log.e("addtopanier", "Erreur lors de l'opération : ${e.message}")
    }







}
suspend fun modifpanier(cartId: String, price: Double) {
    val Response = supabaseClient.from("cart").select(columns = Columns.list("*")) {
        filter {
            eq("id_card", cartId)
        }
    }
    var currentprice = Response.decodeSingleOrNull<Cart>()?.total_price
    if (currentprice != null) {
        currentprice=currentprice + price
    }
    val response = supabaseClient
        .from("cart")
        .update({
                set("total_price" ,currentprice)
}
        ) {
            select()
            filter {

                eq("id_card", cartId)
        }


}}
suspend fun getname_it(itemid: String):String?{
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_item", itemid)
        }
    }

    // Décoder la réponse en un objet User1
    return response.decodeSingleOrNull<Item>()?.id_item
}
suspend fun getprice_it(itemid: String): Double? {
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_item", itemid)
        }
    }

    // Décoder la réponse en un objet User1
    return response.decodeSingleOrNull<Item>()?.price
}
suspend fun getcardproduct(cartid: String): List<compose> {
    println("get cart product $cartid")
    val response=supabaseClient.from("compose").select(columns = Columns.list("*")) {
        filter {
            eq("id_card", cartid)
        }
    }
    println("data ${Gson().toJson(response.data)}")
    return response.decodeList<compose>()

}
suspend fun quantite_modif(itemId:String,cardid:String,quantite:String,totalprice:Double){
    val response = supabaseClient.from("compose").update(
        {
            set("quantity" , quantite)

        }
    ) {
        select()
        filter {
            eq("id_item", itemId)
            eq("id_card", cardid)
        }
    }
    modifprice(cardid,totalprice)

    }
suspend fun create_order(order: order1): Boolean {return try {
        val response = supabaseClient
            .from("order1")
            .insert(order)
       true}
           catch (e: Exception) {
    println("Erreur lors de la création de la commande : ${e.message}")
    false
}
}
suspend fun GetLoca(userId:String):String?{
    val response=supabaseClient.from("user1").select(columns = Columns.list("*")) {
        filter {
            eq("id_user", userId)
        }
    }
   return response.decodeSingleOrNull<User1>()?.location

}

suspend fun modifprice(cartid: String,newprice:Double){
    val response = supabaseClient.from("cart").update(
        {
            set("total_price" ,newprice )

        }
    ) {
        select()
        filter {

            eq("id_card", cartid)
        }
    }
}

suspend fun deletecompose(itemid: String,cartid: String,totalprice:Double){
    val response=supabaseClient.from("compose").delete{
        select()
        filter {
            eq("id_item", itemid)
            eq("id_card", cartid)
    } }
    modifprice(cartid,totalprice)
    }

suspend fun FetchUserOrders(userId: String):List<order1>{
    println("get orders debut ")
    val response=supabaseClient.from("order1").select(columns = Columns.list("*")) {
        filter {
            eq("id_user", userId)
        }
    }
    println("get orders fin ")
    println("data ${Gson().toJson(response.data)}")
    return response.decodeList<order1>()
}

suspend fun fethCartbyid(cartid:String):Cart?{
    val response=supabaseClient.from("cart").select(columns = Columns.list("*")) {


        filter {
            eq("id_card", cartid)
        }
    }
    return response.decodeSingleOrNull<Cart>()
}
suspend fun fetchrestaubyid(restid:String): Restaurant? {
    val response=supabaseClient.from("restaurant").select(columns = Columns.list("*")) {


        filter {
            eq("id_restaurant", restid)
        }
    }
    return response.decodeSingleOrNull<Restaurant>()
}
suspend fun deliveryprice(restid:String): String? {
    val response=supabaseClient.from("restaurant").select(columns = Columns.list("*")) {


        filter {
            eq("id_restaurant", restid)
        }
    }
    return response.decodeSingleOrNull<Restaurant>()?.delivery_price
}

suspend fun getFavoriteRestaurants(userId: String): List<Restaurant>? {
    return try {
        // Récupérer les IDs des restaurants favoris
        val favoriteIdsResponse = supabaseClient
            .from("favori_res")
            .select(Columns.list("id_restaurant")) {
                filter {
                    eq("id_user", userId)
                }
            }

        // Log de la réponse brute pour débogage
        Log.d("getFavoriteRestaurants", "Response: $favoriteIdsResponse")
        val rawJson = favoriteIdsResponse.data
        Log.d("getFavoriteRestaurants", "Raw JSON: $rawJson")

        // Décoder les IDs des restaurants
        val favoriteIds = Json.decodeFromString<List<FavoriteId>>(rawJson.toString())
            .map { it.id_restaurant }

        Log.d("getFavoriteRestaurants", "Favorite IDs: $favoriteIds")
        // Récupérer les détails des restaurants à partir des IDs
        val favoriteRestaurants = favoriteIds.mapNotNull { restaurantId ->
            fetchrestaubyid(restaurantId).also { restaurant ->
                Log.d("getFavoriteRestaurants", "Restaurant: $restaurantId -> $restaurant")
            }
        }

        favoriteRestaurants
    } catch (e: Exception) {
        Log.e("getFavoriteRestaurants", "Erreur : ${e.message}")
        null
    }
}


suspend fun deletefavoris(idUser: String, idRestaurant : String){
    val response=supabaseClient.from("favori_res").delete{
        select()
        filter {
            eq("id_user", idUser)
            eq("id_restaurant", idRestaurant)
        } }

}

suspend fun removeFromFavorites(userId: String , idRestaurant: String) {
    // Appeler la fonction de suppression de la base de données
    deletefavoris(userId, idRestaurant)
}

suspend fun fetchCategories(): List<Category> {
    val response = supabaseClient.postgrest["category"]
        .select(Columns.ALL)
    // Décoder les données
    return response.decodeList<Category>()
}
suspend fun addtofavorit(idUser: String, idRestaurant: String) {
    val newFavorit = favoris_res(
        id_user = idUser,
        id_restaurant = idRestaurant
    )

    try {
        val response = supabaseClient
            .from("favori_res")
            .insert(newFavorit)



    } catch (e: Exception) {
        // Log or rethrow the exception
        e.printStackTrace()
        throw Exception("Failed to add favorite: ${e.message}")
    }
}

suspend fun orderdeliv(idorde:String){
    val response = supabaseClient.from("order1").update(
        {
            set("status" , "Delivered")
        }
    ) {
        select()
        filter {

            eq("id_order", idorde)
        }
    }
}

val authentification = supabaseClient.auth