package br.com.mauker.githubapp.ghrepositories.data.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.mauker.githubapp.STARTING_PAGE_INDEX
import br.com.mauker.githubapp.ghrepositories.domain.entity.GhRepositoryItem
import br.com.mauker.githubapp.ghrepositories.service.GhRepositoriesService
import timber.log.Timber
import javax.inject.Inject

class GhRepoPagingSource @Inject constructor(
    private val service: GhRepositoriesService,
    private val query: String
): PagingSource<Int, GhRepositoryItem>() {

    /**
     * Function that's called asynchronously by the paging lib to fetch more data when needed.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GhRepositoryItem> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        Timber.w("Load called - Query: $query - Page: $pageIndex")

        return try {
            val response = service.getRepositories(
                query = query,
                page = pageIndex
            )

            LoadResult.Page(
                data = response.items,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = pageIndex + 1
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    /**
     * Key used for subsequent load calls after the first load.
     */
    override fun getRefreshKey(state: PagingState<Int, GhRepositoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}