package com.mahirkorac.githubandroid.features.search.repository

import com.mahirkorac.githubandroid.api.NetworkRequests
import com.mahirkorac.githubandroid.features.search.model.AccessToken
import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import javax.inject.Inject
import retrofit2.Response

class SearchRepositoryImpl @Inject constructor(private val networkRequests: NetworkRequests) : SearchRepository {

    override suspend fun getSearchResponse(query: String, filter: String?, order: String?, page: Int?):
            Response<SearchResponse> {
        return filter?.let { filterValue ->
            order?.let { orderValue ->
                networkRequests.getSearchRepositoriesWithFilter(query, filterValue, orderValue, page ?:1)
            }
        } ?: run {
            networkRequests.getSearchRepositories(query, page?:1)
        }
    }

    override suspend fun getAccessToken(clientID: String, clientSecret: String, code: String): AccessToken {
        return networkRequests.getAccessToken(clientID, clientSecret, code)
    }
}