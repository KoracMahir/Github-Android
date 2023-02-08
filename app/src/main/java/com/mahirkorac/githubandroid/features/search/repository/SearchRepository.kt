package com.mahirkorac.githubandroid.features.search.repository

import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import retrofit2.Response

interface SearchRepository {

    suspend fun getSearchResponse(query: String, filter: String?, order: String?): Response<SearchResponse>
}