
import android.util.Log
import com.example.fooddelivery.data.model.Cart
import com.example.fooddelivery.data.model.User1
import com.example.fooddelivery.data.model.compose
import com.example.fooddelivery.data.model.item
import com.example.fooddelivery.data.model.order1
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

suspend fun fetchitembyid(itemid:String):item?{
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_item", itemid)
        }
    }
    // Decode the data into a Restaurant object
    return response.decodeSingleOrNull<item>()
}
//creer un nouveau panier et l'attribuer a un user
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
            // Insérer un nouvel élément
            val insertResponse = supabaseClient
                .from("compose")
                .insert(item)

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
    return response.decodeSingleOrNull<item>()?.id_item
}
suspend fun getprice_it(itemid: String): Double? {
    val response = supabaseClient.from("item").select(columns = Columns.list("*")) {
        filter {
            eq("id_item", itemid)
        }
    }

    // Décoder la réponse en un objet User1
    return response.decodeSingleOrNull<item>()?.price
}
suspend fun getcardproduct(cartid: String):List<compose>?{
    val response=supabaseClient.from("compose").select(columns = Columns.list("*")) {
        filter {
            eq("id_card", cartid)
        }
    }
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