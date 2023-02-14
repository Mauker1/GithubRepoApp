package br.com.mauker.githubapp.ghrepositories.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.mauker.githubapp.DEFAULT_ITEMS_PER_PAGE
import br.com.mauker.githubapp.EMPTY_STRING
import br.com.mauker.githubapp.ghrepositories.domain.GhRepoRepository
import br.com.mauker.githubapp.ghrepositories.domain.entity.GhRepositoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: GhRepoRepository): ViewModel() {

    private var pagerFlow: Flow<PagingData<GhRepositoryItem>>? = null
    private var currentQuery = EMPTY_STRING

    fun restoreGhRepositories(): Flow<PagingData<GhRepositoryItem>>? {
        return pagerFlow
    }

    fun getGhRepositories(query: String): Flow<PagingData<GhRepositoryItem>> {
        if (query == currentQuery) {
            pagerFlow?.let {
                return it
            }
        }
        val pager = Pager(
            config = PagingConfig(
                pageSize = DEFAULT_ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory =  {
                repository.getRepoPagingSource(query)
            }
        )
        val flow = pager.flow.cachedIn(viewModelScope)

        pagerFlow = flow

        return flow
    }
}