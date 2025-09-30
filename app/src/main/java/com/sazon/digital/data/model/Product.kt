
package com.sazon.digital.data.model

data class Product(
    val id: Long = 0,
    val name: String,
    val price: Long, // en centavos
    val imageUrl: String,
    val description: String
)
