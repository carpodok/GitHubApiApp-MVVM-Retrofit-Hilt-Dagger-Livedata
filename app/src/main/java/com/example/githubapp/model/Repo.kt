package com.example.githubapp.model

data class Repo(
    val name: String,
    val owner: Owner,
    val html_url: String
)
