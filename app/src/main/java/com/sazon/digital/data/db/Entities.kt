
package com.sazon.digital.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val price: Long,
    val imageUrl: String,
    val description: String
)

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey val productId: Long
)
