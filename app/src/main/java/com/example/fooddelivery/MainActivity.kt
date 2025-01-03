package com.example.fooddelivery

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.foundation.layout.fillMaxSize


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.ui.screen.CountriesListScreen
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon

import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import com.example.fooddelivery.data.model.Category
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.domain.respository.restoRepository
import com.example.fooddelivery.domain.respository.restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetRestoUsecase

import com.example.fooddelivery.domain.respository.CategoryRepository
import com.example.fooddelivery.domain.respository.CategoryRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetCategoryUseCase
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = restoRepositoryImpl()
        val getrestoUseCase = GetRestoUsecase(repo)

        val categoryrepository = CategoryRepositoryImpl()
        val getcategoryUseCase = GetCategoryUseCase(categoryrepository)

        // Set up the theme and UI content of the app
        setContent {
            FoodDeliveryTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Set the Main Screen content (e.g., a list of countries)

                  //  CategoryList( getCategoryUseCase= getcategoryUseCase)
                   // Spacer(modifier = Modifier.height(24.dp))

                    RestaurantList(getrestoUseCase= getrestoUseCase)
                }
            }
        }
    }
}

//---------------------------Liste des Categories--------------------------//
@Composable
fun CategoryList(
    repository: CategoryRepository = remember { CategoryRepositoryImpl() },
    getCategoryUseCase: GetCategoryUseCase,
) {
    // Détecter l'orientation de l'écran
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == 1 // 1 => Portrait, 2 => Paysage

    // Utiliser LocalContext.current pour obtenir le contexte
    val context = LocalContext.current

    // Variable pour stocker les categories récupérés
    val categories = remember { mutableStateListOf<Category>() }

    // État pour afficher un chargement ou des erreurs
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    // Appeler la fonction fetchCategory pour récupérer les données
    LaunchedEffect(Unit) {

        try {
            isLoading.value = true
            val fetchedCategories = repository.getCategory()
            // Appel à Supabase pour récupérer les restaurants
            categories.clear()
            categories.addAll(fetchedCategories)
        } catch (e: Exception) {
            errorMessage.value = "Failed to load categories: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start= 16.dp, end = 16.dp)

    ) {
        // Titre "All Categories"
        Text(
            text = "All Categories",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            // modifier = Modifier.padding(bottom = 10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage.value != null) {
            Text(
                text = errorMessage.value ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        else {
            // Liste horizontale des catégories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),

                modifier = Modifier
                    .fillMaxWidth() // Prendre toute la largeur disponible
                    .wrapContentHeight() // Ajuster la hauteur pour que le contenu soit visible

            ) {
                items(categories) { category ->
                    CategoryCard(category) // Passe chaque catégorie au composant CategoryCard
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(75.dp) // Largeur de chaque carte
            .height(95.dp) // Hauteur de chaque carte
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFF640D).copy(alpha = 0.05f)) // Couleur de fond avec opacité
            .wrapContentSize(align = Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Image circulaire
            Box(
                modifier = Modifier
                    .size(60.dp) // Taille de l'image
                    .clip(CircleShape)
                    .background(Color.Gray) // Couleur de fond pour l'image
            ) {
                AsyncImage(
                    model = category.image,
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texte de la catégorie
            Text(
                text = category.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
//-------------------------------------------------------------------------//
/*@Composable
fun CategoryList(
    repository: CategoryRepository =  remember { CategoryRepositoryImpl() },
    getcategoryUseCase: GetCategoryUseCase
) {
    // pour obtenir le contexte
    val context = LocalContext.current

    // Variable pour stocker les categories récupérés
    val categories = remember { mutableStateListOf<Category>() }

    // État pour afficher un chargement ou des erreurs
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Appeler la fonction fetchCategory pour récupérer les données
    LaunchedEffect(Unit) {

        try {
            isLoading.value = true

            val fetchedCategories = repository.getCategory()
            // Appel à Supabase pour récupérer les restaurants
            categories.clear()
            categories.addAll(fetchedCategories)
        } catch (e: Exception) {
            errorMessage.value = "Failed to load categories: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    // Détecter l'orientation de l'écran
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == 1 // 1 => Portrait, 2 => Paysage

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start= 16.dp, end = 16.dp)

    ) {
        // Titre "All Categories"
        Text(
            text = "All Categories",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            // modifier = Modifier.padding(bottom = 10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Affichage des données ou du chargement
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage.value != null) {
            Text(
                text = errorMessage.value ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        else {
            // Liste horizontale des catégories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),

                modifier = Modifier
                    .fillMaxWidth() // Prendre toute la largeur disponible
                    .wrapContentHeight() // Ajuster la hauteur pour que le contenu soit visible

            ) {
                items(categories) { category ->
                    CategoryCard(category) // Passe chaque catégorie au composant CategoryCard
                }
            }
        }
    }
}*/
/*@Composable
fun CategoryList(
    repository: CategoryRepository = remember { CategoryRepositoryImpl() },
    getcategoryUseCase: GetCategoryUseCase
) {
    val context = LocalContext.current
    val categories = remember { mutableStateListOf<Category>() }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Fetch categories in the background
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            val fetchedCategories = repository.getCategory()
            categories.clear()
            categories.addAll(fetchedCategories)
        } catch (e: Exception) {
            errorMessage.value = "Failed to load categories: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "All Categories",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Show loading indicator or error message
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            errorMessage.value?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    CategoryCard(category) // Pass each category to CategoryCard composable
                }
            }
        }
    }
}


@Composable
fun CategoryCard(category: Category) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(75.dp) // Largeur de chaque carte
            .height(95.dp) // Hauteur de chaque carte
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFF640D).copy(alpha = 0.05f)) // Couleur de fond avec opacité
            .wrapContentSize(align = Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Image circulaire
            Box(
                modifier = Modifier
                    .size(60.dp) // Taille de l'image
                    .clip(CircleShape)
                    .background(Color.Gray) // Couleur de fond pour l'image
            ) {

                AsyncImage(
                    model = category.image,
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texte de la catégorie
            Text(
                text = category.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
*/
//---------------------------Liste des Restaurants--------------------------//
@Composable
fun RestaurantList(
    repository: restoRepository = remember { restoRepositoryImpl() },
    getrestoUseCase: GetRestoUsecase,
) {
    // Utiliser LocalContext.current pour obtenir le contexte
    val context = LocalContext.current

    // Variable pour stocker les restaurants récupérés
    val restaurants = remember { mutableStateListOf<Restaurant>() }

    // État pour afficher un chargement ou des erreurs
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // État pour gérer le défilement de la liste
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    // Charger les données
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            val fetchedRestaurants = repository.getResto()
            restaurants.clear()
            restaurants.addAll(fetchedRestaurants)
        } catch (e: Exception) {
            errorMessage.value = "Failed to load restaurants: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Affichage du titre et du bouton de défilement
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Most popular",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Right list icon",
                    tint = Color.Gray,
                    modifier = Modifier
                        .clickable {
                            // Défilement vers le prochain élément lorsque la flèche est cliquée
                            coroutineScope.launch {
                                val currentIndex = listState.firstVisibleItemIndex
                                val nextIndex = (currentIndex + 1).coerceAtMost(restaurants.size - 1)
                                listState.animateScrollToItem(nextIndex)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Affichage des données ou du chargement
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage.value != null) {
            Text(
                text = errorMessage.value ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Liste horizontale des restaurants
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(restaurants) { restaurant ->
                    RestaurantCard(restaurant)
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    // État pour suivre si le restaurant est favori ou non
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Image du restaurant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = restaurant.logo,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nom du restaurant et icône de favoris
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = restaurant.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite icon",
                tint = if (isFavorite) Color(0xFFFF640D) else Color.Gray,
                modifier = Modifier
                    .clickable {
                        isFavorite = !isFavorite // Changement d'état
                    }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Delivery food: ${restaurant.delivery_price}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = restaurant.delivery_time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF640D)
        )
    }
}

@Composable
fun AsyncImage(model: String, contentDescription: String, contentScale: ContentScale, modifier: Modifier) {

}


