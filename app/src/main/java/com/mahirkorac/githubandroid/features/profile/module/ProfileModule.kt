package com.mahirkorac.githubandroid.features.profile.module

import com.mahirkorac.githubandroid.features.profile.repository.ProfileRepository
import com.mahirkorac.githubandroid.features.profile.repository.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class ProfileModule {

    @Binds
    abstract fun getUserSource(repo: ProfileRepositoryImpl): ProfileRepository
}