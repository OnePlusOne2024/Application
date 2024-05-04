package com.example.oneplusone.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteProductModel::class], version = 1, exportSchema = false)
abstract class DbHelper: RoomDatabase() {

    abstract fun favoriteProductDao(): FavoriteProductDao


    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {

            }
        }

        @Volatile
        private var INSTANCE: DbHelper? = null

        fun getDatabase(context: Context): DbHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbHelper::class.java,
                    "OnePlusOneDataBase"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}