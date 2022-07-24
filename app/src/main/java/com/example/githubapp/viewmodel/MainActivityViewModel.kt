package com.example.githubapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.model.Repo
import com.example.githubapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private var liveDataList: MutableLiveData<List<Repo>> = MutableLiveData()

    fun getLiveData(): MutableLiveData<List<Repo>> {
        return liveDataList
    }

    fun loadData(userName: String) {
        repository.makeApiCall(userName, liveDataList)
    }
}