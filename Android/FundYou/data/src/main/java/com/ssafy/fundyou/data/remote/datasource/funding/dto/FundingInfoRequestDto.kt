package com.ssafy.fundyou.data.remote.datasource.funding.dto

import com.google.gson.annotations.SerializedName

internal data class FundingInfoRequestDto(
    @SerializedName("funding_id")
    val fundingId : Long
)