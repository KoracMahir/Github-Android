package com.mahirkorac.githubandroid.features.profile.repository

import com.mahirkorac.githubandroid.features.profile.model.UserProfile
import retrofit2.Response

interface ProfileRepository {

    suspend fun getCurrentUserProfile(token: String): Response<UserProfile>
}