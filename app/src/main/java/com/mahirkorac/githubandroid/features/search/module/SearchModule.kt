package com.mahirkorac.githubandroid.features.search.module

import com.mahirkorac.githubandroid.features.search.repository.SearchRepository
import com.mahirkorac.githubandroid.features.search.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class SearchModule {

    @Binds
    abstract fun getProfileSource(repo: SearchRepositoryImpl): SearchRepository
}