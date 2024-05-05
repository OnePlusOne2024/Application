package com.example.oneplusone.mudule

import android.content.Context
import androidx.room.Room
import com.example.oneplusone.db.FavoriteProductDao
import com.example.oneplusone.db.ProductDataBase
import com.example.oneplusone.repository.FavoriteProductRepository
import com.example.oneplusone.repository.FavoriteProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =

        Room.databaseBuilder(
            context,
            ProductDataBase::class.java,
            "OnePlusOneDataBase"
        ).fallbackToDestructiveMigration()
            .build()



    @Provides
    fun provideUserDao(productDataBase: ProductDataBase) = productDataBase.favoriteProductDao()


    @Provides
    fun provideFavoriteProductRepository(favoriteProductDao: FavoriteProductDao): FavoriteProductRepository {
        return FavoriteProductRepositoryImpl(favoriteProductDao)
    }
}