package com.example.oneplusone.mudule

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS favoriteProduct")

            // 새 테이블 생성
            db.execSQL("""
            CREATE TABLE favoriteProduct (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                productName TEXT NOT NULL,
                productPrice INTEGER NOT NULL,
                brand TEXT NOT NULL,
                benefits TEXT NOT NULL,
                productImage TEXT NOT NULL,
                favorite INTEGER NOT NULL,
                category TEXT NOT NULL,
                pb INTEGER NOT NULL
            )
        """)

            db.execSQL(
                "CREATE TABLE IF NOT EXISTS productData " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "productName TEXT NOT NULL," +
                    "productPrice INTEGER NOT NULL," +
                    "brand TEXT NOT NULL," +
                    "benefits TEXT NOT NULL," +
                    "productImage TEXT NOT NULL," +
                    "favorite INTEGER NOT NULL," +
                    "category TEXT NOT NULL," +
                    "pb INTEGER NOT NULL)")
        }

    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =

        Room.databaseBuilder(
            context,
            ProductDataBase::class.java,
            "OnePlusOneDataBase"
        )   .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()



    @Provides
    fun provideUserDao(productDataBase: ProductDataBase) = productDataBase.favoriteProductDao()

    @Provides
    fun provideProduct(productDataBase: ProductDataBase) = productDataBase.productDataDao()

    @Provides
    fun provideFavoriteProductRepository(favoriteProductDao: FavoriteProductDao): FavoriteProductRepository {
        return FavoriteProductRepositoryImpl(favoriteProductDao)
    }
}