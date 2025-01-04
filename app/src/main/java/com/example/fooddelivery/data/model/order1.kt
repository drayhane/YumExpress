package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class order1(
    val id_order:String,
    val datee: String,
    val payent_meth:String,
    val status:String?,
    val delivery_adress:String?,
    val delevery_note:String?,
    val id_user:String,
    val Id_rest:String?,
    val id_card:String,

    )

