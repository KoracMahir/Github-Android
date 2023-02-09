package com.mahirkorac.githubandroid.features.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahirkorac.githubandroid.ConsumableLiveData
import com.mahirkorac.githubandroid.api.DataHandler
import com.mahirkorac.githubandroid.features.profile.model.UserProfile
import com.mahirkorac.githubandroid.features.profile.repository.ProfileRepository
import com.mahirkorac.githubandroid.features.search.model.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Response

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
) : ViewModel() {

    private val _user = ConsumableLiveData<DataHandler<UserProfile>>()
    val user: LiveData<DataHandler<UserProfile>> = _user

    fun getCurrentUserProfile(token: String) {
        _user.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            if(token.isNotEmpty()){
                val response = repo.getCurrentUserProfile(token)
                _user.postValue(handleResponse(response))
            }
        }
    }

    private fun handleResponse(response: Response<UserProfile>): DataHandler<UserProfile> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                return DataHandler.SUCCESS(it,false)
            }
        }
        return DataHandler.ERROR(message = "Something went wrong", loading = false)
    }

}