package com.ssafy.fundyou.data.remote.datasource.funding.dto

import com.google.gson.annotations.SerializedName

internal data class FundingSimpleResponseDto(
    @SerializedName("currentFundingPrice")
    val currentFundingPrice: Int?,
    @SerializedName("fundingName")
    val fundingName : String?,
    @SerializedName("startDate")
    val startDate : Long?,
    @SerializedName("endDate")
    val endDate: Long?,
    @SerializedName("fundingStatus")
    val fundingStatus: Boolean?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("percentage")
    val percentage: Int?,
    @SerializedName("totalPrice")
    val totalPrice: Int?
)