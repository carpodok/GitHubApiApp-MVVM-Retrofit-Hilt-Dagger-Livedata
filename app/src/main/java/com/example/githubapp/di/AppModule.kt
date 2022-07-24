package com.example.githubapp.di

import com.example.githubapp.service.ApiService
import com.example.githubapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val baseURL = Constants.BASE_URL

    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}