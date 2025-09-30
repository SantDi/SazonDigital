
package com.sazon.digital.di

import android.content.Context
import androidx.room.Room
import com.sazon.digital.data.db.AppDatabase
import com.sazon.digital.data.db.CartDao
import com.sazon.digital.data.db.ProductDao
import com.sazon.digital.data.prefs.UserPrefs
import com.sazon.digital.data.repo.StoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "sazon.db").build()

    @Provides fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
    @Provides fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides @Singleton
    fun provideRepository(productDao: ProductDao, cartDao: CartDao): StoreRepository =
        StoreRepository(productDao, cartDao)

    @Provides @Singleton
    fun providePrefs(@ApplicationContext ctx: Context): UserPrefs = UserPrefs(ctx)
}
