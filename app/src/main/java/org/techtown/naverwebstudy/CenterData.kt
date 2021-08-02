package org.techtown.naverwebstudy

data class CenterData(
    val currentCount: Int,
    val `data`: List<Items>,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)