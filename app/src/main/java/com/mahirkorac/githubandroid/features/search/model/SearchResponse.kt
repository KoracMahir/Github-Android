package com.mahirkorac.githubandroid.features.search.model

data class SearchResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)