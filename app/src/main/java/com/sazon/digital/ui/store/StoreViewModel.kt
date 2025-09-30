
package com.sazon.digital.ui.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sazon.digital.data.db.ProductEntity
import com.sazon.digital.data.repo.StoreRepository
import com.sazon.digital.data.prefs.UserPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repo: StoreRepository,
    private val prefs: UserPrefs
) : ViewModel() {

    private val query = MutableStateFlow<String?>(null)

    val gridSize = prefs.gridSize.stateIn(viewModelScope, SharingStarted.Eagerly, 160)

    val products: StateFlow<List<ProductEntity>> =
        query.flatMapLatest { q -> repo.products(q) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val cart = repo.cart().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch { repo.seedIfEmpty() }
    }

    fun setQuery(q: String) { query.value = q }

    fun toggleCart(id: Long) {
        viewModelScope.launch {
            val inCart = repo.inCart(id).first()
            if (inCart) repo.removeFromCart(id) else repo.addToCart(id)
        }
    }
}
