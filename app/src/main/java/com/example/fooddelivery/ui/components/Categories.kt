package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.data.model.Category
import com.example.fooddelivery.domain.respository.CategoryRepository
import com.example.fooddelivery.domain.respository.CategoryRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetCategoryUseCase

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
                AsyncImage_(
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
@Composable
fun AsyncImage_(model: String, contentDescription: String, contentScale: ContentScale, modifier: Modifier) {

}