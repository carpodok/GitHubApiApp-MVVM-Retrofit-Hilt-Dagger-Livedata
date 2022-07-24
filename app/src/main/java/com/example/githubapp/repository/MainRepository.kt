package com.example.githubapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.githubapp.model.Repo
import com.example.githubapp.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun makeApiCall(userName: String, liveDataList: MutableLiveData<List<Repo>>) {
        val call: Call<List<Repo>> = apiService.getData(userName)
        call.enqueue(object : Callback<List<Repo>> {
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                liveDataList.postValue(null)
            }

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                liveDataList.postValue(response.body())
            }
        })
    }

}