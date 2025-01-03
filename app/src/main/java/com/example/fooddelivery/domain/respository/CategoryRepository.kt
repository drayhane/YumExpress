package com.example.fooddelivery.domain.respository

import com.example.fooddelivery.data.model.Category
import fetchCategory


interface CategoryRepository {
    suspend fun getCategory(): List<Category>
    suspend fun syncCategory(remoteR: List<Category>)
}

class CategoryRepositoryImpl : CategoryRepository{

    override suspend fun getCategory(): List<Category> {
        return fetchCategory()
    }

    override suspend fun syncCategory(remoteR: List<Category>) {
    }
}