package br.com.mauker.githubapp.ghrepositories.service

import br.com.mauker.githubapp.ACCEPT_JSON_GH_V3
import br.com.mauker.githubapp.DEFAULT_ITEMS_PER_PAGE
import br.com.mauker.githubapp.HEADER_ACCEPT
import br.com.mauker.githubapp.STARTING_PAGE_INDEX
import br.com.mauker.githubapp.ghrepositories.domain.entity.GhRepoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GhRepositoriesService {

    @GET(PATH_REPOSITORIES)
    suspend fun getRepositories(
        @Header(HEADER_ACCEPT) accept: String = ACCEPT_JSON_GH_V3,
        @Query(PARAM_QUERY) query: String,
        @Query(PARAM_PAGE) page: Int = STARTING_PAGE_INDEX,
        @Query(PARAM_PER_PAGE) perPage: Int = DEFAULT_ITEMS_PER_PAGE,
    ): GhRepoResponse

    companion object {
        private const val PATH_REPOSITORIES = "search/repositories"
        private const val PARAM_QUERY = "q"
        private const val PARAM_PER_PAGE = "per_page"
        private const val PARAM_PAGE = "page"
    }
}