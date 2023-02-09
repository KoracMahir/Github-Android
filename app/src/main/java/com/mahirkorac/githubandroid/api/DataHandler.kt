package com.mahirkorac.githubandroid.api

sealed class DataHandler<T>(
    val data: T? = null,
    val message: String? = null,
    val loading: Boolean = true
) {
    class SUCCESS<T>(data: T, loading: Boolean) : DataHandler<T>(data, loading = loading)
    class ERROR<T>(data: T? = null, message: String, loading: Boolean) : DataHandler<T>(data, message, loading = loading)
    class LOADING<T> : DataHandler<T>()

}