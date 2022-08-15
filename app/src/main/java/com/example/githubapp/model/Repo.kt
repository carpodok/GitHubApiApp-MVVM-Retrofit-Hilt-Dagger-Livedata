package com.example.githubapp.model

import com.google.gson.annotations.SerializedName

data class Repo(

    @SerializedName("name")
    val repoName: String,

    val owner: Owner,

    @SerializedName("html_url")
    val htmlUrl: String
)
