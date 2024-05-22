package com.example.oneplusone.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteProductModel::class,ProductData::class], version = 2, exportSchema = false)
abstract class ProductDataBase: RoomDatabase() {

    abstract fun favoriteProductDao(): FavoriteProductDao

    abstract fun productDataDao(): ProductDao


//    companion object {
////        private val MIGRATION_1_2 = object : Migration(1, 2) {
////            override fun migrate(db: SupportSQLiteDatabase) {
////
////            }
////        }
//
//        @Volatile
//        private var INSTANCE: ProductDataBase? = null
//
//        fun getDatabase(context: Context): ProductDataBase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    ProductDataBase::class.java,
//                    "OnePlusOneDataBase"
//                )
//                    .addMigrations()
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}