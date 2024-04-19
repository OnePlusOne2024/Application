package com.example.oneplusone.mudule

import androidx.lifecycle.MutableLiveData
import com.example.oneplusone.model.data.MainFilterData
import com.example.oneplusone.repository.FilterDataRepository
import com.example.oneplusone.repository.FilterDataRepositoryImpl
import com.example.oneplusone.repository.MainFilterRepository
import com.example.oneplusone.repository.MainFilterRepositoryImpl
import com.example.oneplusone.repository.ProductDataRepository
import com.example.oneplusone.repository.ProductDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMainFilterRepository(): MainFilterRepository = MainFilterRepositoryImpl()

    @Provides
    fun provideFilterDataRepository(): FilterDataRepository = FilterDataRepositoryImpl()

    @Provides
    fun provideProductDataRepository(): ProductDataRepository = ProductDataRepositoryImpl()
}