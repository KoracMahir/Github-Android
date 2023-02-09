package com.mahirkorac.githubandroid.features.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahirkorac.githubandroid.ConsumableLiveData
import com.mahirkorac.githubandroid.api.DataHandler
import com.mahirkorac.githubandroid.features.filter.model.OrderType
import com.mahirkorac.githubandroid.features.filter.model.SortType
import com.mahirkorac.githubandroid.features.search.model.AccessToken
import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import com.mahirkorac.githubandroid.features.search.repository.SearchRepository
import com.mahirkorac.githubandroid.utils.Constants.clientID
import com.mahirkorac.githubandroid.utils.Constants.clientSecret
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Response

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepository
) : ViewModel() {

    private val _clearData = MutableLiveData(true)
    val clearData: LiveData<Boolean> = _clearData

    private val _page = MutableLiveData(1)
    val page: LiveData<Int> = _page

    private val _accessToken = MutableLiveData<AccessToken>()
    val accessToken: LiveData<AccessToken> = _accessToken

    private val query = ConsumableLiveData<String?>(true)
    val queryValue: LiveData<String?> = query
    private val sort = ConsumableLiveData<String?>(true)
    val sortValue: LiveData<String?> = sort
    private val order = ConsumableLiveData<String?>(true)
    val orderValue: LiveData<String?> = order

    private val _repositories = ConsumableLiveData<DataHandler<SearchResponse>>()
    val repositories: LiveData<DataHandler<SearchResponse>> = _repositories

    fun getSearchRepositories() {
        viewModelScope.launch {
            if (query.consume) {
                queryValue.value?.let { queryParameter ->
                    _clearData.postValue(false)
                    val response = repo.getSearchResponse(
                        queryParameter,
                        sortValue.value,
                        orderValue.value,
                        page.value
                    )
                    _repositories.postValue(handleResponse(response))
                    _clearData.postValue(true)
                }
            }
        }
    }

    fun updateQuery(search: String) {
        query.postValue(search)
    }

    fun updateFilter(sortType: SortType?, orderType: OrderType?) {
        sort.postValue(sortType?.value)
        order.postValue(orderType?.value)
    }

    fun loadMore(){
        var currentPage = page.value ?: 0
        _page.postValue(++currentPage)
    }

    private fun handleResponse(response: Response<SearchResponse>): DataHandler<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                return DataHandler.SUCCESS(it, false)
            }
        }
        return DataHandler.ERROR(message = "Something went wrong", loading = false)
    }

    fun clearSearch() {
        _repositories.postValue(DataHandler.ERROR(message = "Empty search", loading = false))
        query.postValue(null)
        _clearData.postValue(true)
    }

    fun getAccessToken(code: String) {
        viewModelScope.launch {
            try {
                _accessToken.value = repo.getAccessToken(clientID, clientSecret, code)
            } catch (_: Exception) {}
        }
    }

}