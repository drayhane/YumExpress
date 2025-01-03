package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.domain.respository.CategoryRepositoryImpl
import com.example.fooddelivery.domain.respository.restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetCategoryUseCase
import com.example.fooddelivery.domain.usecase.GetRestoUsecase
import com.example.fooddelivery.ui.components.LocationInfo
import com.example.fooddelivery.ui.components.SearchBar
import com.example.fooddelivery.ui.components.OfferFood
import com.example.fooddelivery.ui.components.CategoryList
import com.example.fooddelivery.ui.components.RestaurantList



@Composable
fun HomeScreen() {
    val repo = restoRepositoryImpl()
    val getrestoUseCase = GetRestoUsecase(repo)

    val categoryrepository = CategoryRepositoryImpl()
    val getcategoryUseCase = GetCategoryUseCase(categoryrepository)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()), // Permettre le scroll vertical,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationInfo()
        SearchBar()
       OfferFood()
        CategoryList( getCategoryUseCase= getcategoryUseCase)
       RestaurantList(getrestoUseCase = getrestoUseCase)
    }
}