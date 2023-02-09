package com.mahirkorac.githubandroid.api

import com.mahirkorac.githubandroid.features.profile.model.UserProfile
import com.mahirkorac.githubandroid.features.search.model.AccessToken
import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import com.mahirkorac.githubandroid.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkRequests {

    @GET("search/repositories?")
    suspend fun getSearchRepositories(@Query("q") query: String): Response<SearchResponse>

    @GET("search/repositories?")
    suspend fun getSearchRepositoriesWithFilter(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Response<SearchResponse>

    @Headers("Accept: application/json")
    @POST(Constants.domainURL + "login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): AccessToken

    @GET("/user")
    suspend fun getCurrentUserProfile(
        @Header("Authorization") authHeader: String
    ): Response<UserProfile>
}