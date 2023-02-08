package com.mahirkorac.githubandroid.api

import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkRequests {

    @GET("search/repositories?")
    suspend fun getSearchRepositories(@Query("q") query: String): Response<SearchResponse>
    @GET("search/repositories?")
    suspend fun getSearchRepositoriesWithFilter(@Query("q") query: String,
                                      @Query("sort") sort: String,
                                      @Query("order") order: String): Response<SearchResponse>
}