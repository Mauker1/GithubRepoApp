package br.com.mauker.githubapp.ghrepositories.domain

import br.com.mauker.githubapp.ghrepositories.data.datasources.GhRepoPagingSource

interface GhRepoRepository {
    fun getRepoPagingSource(query: String): GhRepoPagingSource
}