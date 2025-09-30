
package com.sazon.digital.data.repo

import com.sazon.digital.data.db.*
import kotlinx.coroutines.flow.Flow

class StoreRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {
    fun products(query: String?): Flow<List<ProductEntity>> =
        if (query.isNullOrBlank()) productDao.getAll() else productDao.search(query)

    fun cart(): Flow<List<CartItemEntity>> = cartDao.getAll()

    fun inCart(productId: Long): Flow<Boolean> = cartDao.inCart(productId)

    suspend fun addToCart(id: Long) = cartDao.add(CartItemEntity(productId = id))

    suspend fun removeFromCart(id: Long) = cartDao.remove(CartItemEntity(productId = id))

    suspend fun seedIfEmpty() {
        if (productDao.count() == 0) {
            productDao.insertAll(
                listOf(
                    ProductEntity(name = "Sartén 28cm", price = 99900, imageUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba", description = "Sartén antiadherente 28 cm."),
                    ProductEntity(name = "Cuchillo Chef", price = 59900, imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947", description = "Cuchillo de chef 8\"."),
                    ProductEntity(name = "Tabla Bambú", price = 39900, imageUrl = "https://images.unsplash.com/photo-1556910103-1c02745aae4d", description = "Tabla de cortar de bambú."),
                    ProductEntity(name = "Wok Antiad.", price = 129900, imageUrl = "https://images.unsplash.com/photo-1526318472351-c75fcf070305", description = "Wok antiadherente familiar.")
                )
            )
        }
    }
}
