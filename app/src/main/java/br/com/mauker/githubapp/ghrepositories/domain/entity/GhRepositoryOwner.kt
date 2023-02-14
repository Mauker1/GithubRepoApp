package br.com.mauker.githubapp.ghrepositories.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GhRepositoryOwner(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "login") val name: String,
    @field:Json(name = "avatar_url") val avatarUrl: String,
)
