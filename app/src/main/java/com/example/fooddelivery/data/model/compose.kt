package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class compose(
    val id_item: String,
    var id_card: String,
    val quantity: String,
    val supp: String?,
    val sauce: String?,
    val size: String?,
)