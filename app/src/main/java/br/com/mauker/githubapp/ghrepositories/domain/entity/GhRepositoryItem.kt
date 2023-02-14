package br.com.mauker.githubapp.ghrepositories.domain.entity

import br.com.mauker.githubapp.EMPTY_STRING
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// For the sake of brevity, I'll use this class both for the data layer and the UI layer.
@JsonClass(generateAdapter = true)
data class GhRepositoryItem(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "full_name") val title: String,
    @field:Json(name = "description") val description: String? = EMPTY_STRING,
    @field:Json(name = "html_url") val url: String,
    @field:Json(name = "owner") val owner: GhRepositoryOwner,
)