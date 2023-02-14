package br.com.mauker.githubapp.ghrepositories.di

import br.com.mauker.githubapp.ghrepositories.domain.GhRepoRepository
import br.com.mauker.githubapp.ghrepositories.data.repository.GhRepoRepositoryImpl
import br.com.mauker.githubapp.ghrepositories.service.GhRepositoriesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GhRepoModule {
    @ViewModelScoped
    @Provides
    fun provideGhRepository(service: GhRepositoriesService): GhRepoRepository = GhRepoRepositoryImpl(service)
}