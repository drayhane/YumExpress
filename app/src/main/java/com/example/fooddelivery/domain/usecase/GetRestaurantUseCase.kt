package com.example.fooddelivery.domain.usecase


import com.example.fooddelivery.data.model.Restaurant
import  com.example.fooddelivery.domain.respository.restoRepository

class GetRestoUsecase(private val repository: restoRepository) {
    suspend operator fun invoke(): List<Restaurant> {
        return repository.getResto()
    }
}