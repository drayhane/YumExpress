package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fooddelivery.domain.respository.CategoryRepositoryImpl
import com.example.fooddelivery.domain.respository.restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetCategoriesUseCase
import com.example.fooddelivery.domain.usecase.GetRestoUsecase
import com.example.fooddelivery.ui.components.LocationInfo
import com.example.fooddelivery.ui.components.SearchBar
import com.example.fooddelivery.ui.components.OfferFood
import com.example.fooddelivery.ui.components.CategoryList
import com.example.fooddelivery.ui.components.RestaurantList

/*@Composable
fun HomeScreen() {
    val repo = restoRepositoryImpl()
    val getrestoUseCase = GetRestoUsecase(repo)

    val categoryrepository = CategoryRepositoryImpl()
    val getcategoryUseCase = GetCategoriesUseCase(categoryrepository)

    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationInfo()
        SearchBar() // Pass the search text handler
        OfferFood()
        CategoryList(getCategoriesUseCase = getcategoryUseCase)
        RestaurantList(getrestoUseCase = getrestoUseCase) // Filtered list based on searchText
    }
}
*/

@Composable
fun HomeScreen(navController: NavHostController) {
    val repo = restoRepositoryImpl()
    val getrestoUseCase = GetRestoUsecase(repo)
    val selectedCategory = remember { mutableStateOf<String?>(null) }
    val selectedRestaurantId = remember { mutableStateOf<String?>(null) }

    val categoryrepository = CategoryRepositoryImpl()
    val getcategoryUseCase = GetCategoriesUseCase(categoryrepository)

    var searchText by remember { mutableStateOf("") }
    val resetCategory = remember { mutableStateOf(false) } // Flag to reset the category when "Most Popular" is clicked


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()), // Permettre le scroll vertical,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationInfo()
        SearchBar(onSearchTextChanged = { searchText = it }) // Pass the search text handler
        OfferFood()
        /*CategoryList(getCategoriesUseCase = getcategoryUseCase, onCategorySelected = { categoryName ->
            selectedCategory.value = categoryName // Filtrer les restaurants en fonction de la catégorie sélectionnée
        })*/
        CategoryList(getCategoriesUseCase = getcategoryUseCase, onCategorySelected = { categoryName ->
            selectedCategory.value = categoryName // Filtrer les restaurants en fonction de la catégorie sélectionnée

        },
            selectedCategory = selectedCategory.value,)
        RestaurantList(
            getrestoUseCase = getrestoUseCase,
            searchText = searchText,
            selectedCategory = selectedCategory.value,
            //resetCategory = resetCategory.value,
            onCategorySelected = { categoryName ->
                selectedCategory.value = categoryName
                resetCategory.value = true // Set the flag to reset the category
            },
            navController = navController
        )
       // RestaurantList(getrestoUseCase = getrestoUseCase, searchText = searchText, selectedCategory = selectedCategory.value) // Filtered list based on searchText
    }
}


