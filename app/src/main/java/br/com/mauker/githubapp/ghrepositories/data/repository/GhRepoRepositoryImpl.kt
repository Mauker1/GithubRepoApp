package br.com.mauker.githubapp.ghrepositories.data.repository

import br.com.mauker.githubapp.ghrepositories.data.datasources.GhRepoPagingSource
import br.com.mauker.githubapp.ghrepositories.domain.GhRepoRepository
import br.com.mauker.githubapp.ghrepositories.service.GhRepositoriesService
import javax.inject.Inject

class GhRepoRepositoryImpl @Inject constructor(private val service: GhRepositoriesService):
    GhRepoRepository {
    override fun getRepoPagingSource(query: String) = GhRepoPagingSource(service = service, query = query)
}