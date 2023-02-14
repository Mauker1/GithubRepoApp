package br.com.mauker.githubapp.ghrepositories.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GhRepoResponse (
    @field:Json(name = "total_count") val totalCount: Int,
    @field:Json(name = "incomplete_results") val incompleteResults: Boolean,
    @field:Json(name = "items") val items: List<GhRepositoryItem>,
)