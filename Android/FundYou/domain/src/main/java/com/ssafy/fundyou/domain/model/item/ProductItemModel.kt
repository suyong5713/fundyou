package com.ssafy.fundyou.domain.model.item

data class ProductItemModel(
    val id: Long,
    val price: Int,
    val img: List<String>,
    val descriptionImg: String,
    val title: String,
    val isAr: Boolean,
    val isFavorite: Boolean,
    val description: List<ProductDescriptionModel>?,
    val sellingCount: Int,
    val brand: String,
    val category: ProductCategoryModel
)