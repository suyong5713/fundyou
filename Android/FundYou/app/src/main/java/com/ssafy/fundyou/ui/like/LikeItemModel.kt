package com.ssafy.fundyou.ui.like

import com.ssafy.fundyou.domain.model.item.ProductItemModel

data class LikeItemModel(
    val id: Long,
    val title: String,
    val brand: String,
    val price: Int,
    val img: String,
    val isAr: Boolean
)

fun ProductItemModel.toLikeItemModel() = LikeItemModel(
    id =  this.id,
    title = this.title,
    brand = this.brand,
    price = this.price,
    img = this.img[0],
    isAr = this.isAr
)