package com.example.fooddelivery.domain.respository

import GetLoca
import com.example.fooddelivery.data.model.User1
import fetchUpdateUser
import fetchUserById

interface UserRepository {
    suspend fun getUserById(userId: String): User1?
    suspend fun updateUser(userId: String, updatedUser: User1): User1?
    suspend fun GetUserLocation(userId: String):String?
}
class UserRepositoryImpl : UserRepository {

    override suspend fun getUserById(userId: String): User1? {
        return fetchUserById(userId)
    }
    override suspend fun updateUser(userId: String, updatedUser: User1): User1?{
        return fetchUpdateUser(userId, updatedUser)
    }
    override suspend fun GetUserLocation(userId: String):String?{
        return GetLoca(userId)
    }


    }

