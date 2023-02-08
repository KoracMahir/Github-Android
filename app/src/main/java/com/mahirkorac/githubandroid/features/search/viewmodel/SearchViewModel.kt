package com.mahirkorac.githubandroid.features.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahirkorac.githubandroid.api.DataHandler
import com.mahirkorac.githubandroid.features.filter.model.OrderType
import com.mahirkorac.githubandroid.features.filter.model.SortType
import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import com.mahirkorac.githubandroid.features.search.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Response

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepository
) : ViewModel() {

    private val query = MutableLiveData<String>()
    val queryValue : LiveData<String> = query
    private val sort = MutableLiveData<String>()
    val sortValue : LiveData<String> = sort
    private val order = MutableLiveData<String>()
    val orderValue : LiveData<String> = order

    private val _repositories = MutableLiveData<DataHandler<SearchResponse>>()
    val repositories: LiveData<DataHandler<SearchResponse>> = _repositories

    fun getSearchRepositories() {
        _repositories.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            queryValue.value?.let { queryParameter ->
                val response = repo.getSearchResponse(
                    queryParameter,
                    sortValue.value,
                    orderValue.value
                )
                _repositories.postValue(handleResponse(response))
            }
        }
    }

    fun updateQuery(search: String) {
        query.postValue(search)
    }

    fun updateFilter(sortType: SortType, orderType: OrderType) {
        sort.postValue(sortType.value)
        order.postValue(orderType.value)
    }

    private fun handleResponse(response: Response<SearchResponse>): DataHandler<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                return DataHandler.SUCCESS(it)
            }
        }
        return DataHandler.ERROR(message = "Something went wrong")
    }

    fun clearSearch() {
        _repositories.postValue(DataHandler.ERROR(message = "Empty search"))
    }

}