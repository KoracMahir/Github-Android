package com.mahirkorac.githubandroid.features.search.model

data class SearchPayload(
    val filter: String?,
    val query: String,
    val order: String?
)