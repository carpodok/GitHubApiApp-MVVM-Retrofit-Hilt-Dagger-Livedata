package com.example.githubapp.service

import com.example.githubapp.model.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("users/{user}/repos")
    fun getData(@Path("user") user: String): Call<List<Repo>>


}