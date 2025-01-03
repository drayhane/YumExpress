package com.example.fooddelivery.domain.usecase

import com.example.fooddelivery.data.model.Category
import com.example.fooddelivery.domain.respository.CategoryRepository


class GetCategoryUseCase(private val repository: CategoryRepository){
    suspend operator fun invoke(): List<Category> {
        return  repository.getCategory()
    }
}