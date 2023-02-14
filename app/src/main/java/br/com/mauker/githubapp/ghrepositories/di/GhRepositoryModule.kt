package br.com.mauker.githubapp.ghrepositories.di

import br.com.mauker.githubapp.ghrepositories.domain.GhRepoRepository
import br.com.mauker.githubapp.ghrepositories.data.repository.GhRepoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class GhRepositoryModule {
    @Binds
    abstract fun bindGhRepository(
        impl: GhRepoRepositoryImpl
    ): GhRepoRepository
}