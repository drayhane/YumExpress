package com.example.fooddelivery.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.data.model.Category
import com.example.fooddelivery.domain.respository.CategoryRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetCategoriesUseCase
import coil.compose.rememberAsyncImagePainter

//---------------------------Liste des Catégories--------------------------//
@Composable
fun CategoryList(
    repository: CategoryRepositoryImpl = remember { CategoryRepositoryImpl() },
    getCategoriesUseCase: GetCategoriesUseCase = remember { GetCategoriesUseCase(repository) }, //pour récupérer les catégories
    onCategorySelected: (String?) -> Unit, //pour la sélection de catégories
    selectedCategory: String? //pour une catégorie sélectionnée
) {
    // Etat pour la liste mutable des catégories récupérées
    val categories = remember { mutableStateListOf<Category>() }

    // Indicateur de chargement
    val isLoading = remember { mutableStateOf(true) }

    // Message d'erreur en cas de problème
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Effet déclenché une seule fois pour récupérer les catégories
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true // Afficher le chargement
            val fetchedCategories = getCategoriesUseCase() //pour get les catégories
            categories.clear()
            categories.addAll(fetchedCategories)
        } catch (e: Exception) {
            errorMessage.value = "Failed to load categories: ${e.message}" // Gérer les erreurs
        } finally {
            isLoading.value = false // Pour arrêter l'indicateur de chargement
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        // Titre de la section catégorie
        Text(
            text = "All Categories",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Gestion de l'état d'affichage
        if (isLoading.value) {
            // Affichage de l'indicateur de chargement
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage.value != null) {
            // Affichage du message d'erreur
            Text(
                text = errorMessage.value ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Liste horizontale des catégories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Affichage des catégories
                items(categories) { category ->
                    CategoryCard(
                        category,
                        onClick = { onCategorySelected(category.name) },
                        isSelected = selectedCategory == category.name // Vérification si la catégorie est sélectionnée
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit, //lorsque la catégorie est cliquée
    isSelected: Boolean // Indicateur si cette catégorie est sélectionnée
) {
    // UI
    Box(
        modifier = Modifier
            .width(75.dp)
            .height(95.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Color(0xFFFF640D).copy(alpha = 0.1f)
                else Color.Transparent
            )
            .wrapContentSize(align = Alignment.Center)
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(category.image),
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
