package com.mahirkorac.githubandroid.features.profile.repository

import com.mahirkorac.githubandroid.api.NetworkRequests
import com.mahirkorac.githubandroid.features.profile.model.UserProfile
import javax.inject.Inject
import retrofit2.Response

class ProfileRepositoryImpl @Inject constructor(private val networkRequests: NetworkRequests) : ProfileRepository {

    override suspend fun getCurrentUserProfile(token: String): Response<UserProfile> {
        return networkRequests.getCurrentUserProfile("Bearer $token")
    }

}