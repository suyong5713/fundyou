package com.ssafy.fundyou.data.remote.service

import com.ssafy.fundyou.data.remote.datasource.auth.dto.GoogleAuthRequestDto
import com.ssafy.fundyou.data.remote.datasource.auth.dto.GoogleAuthResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("oauth2/v4/token")
    suspend fun getGoogleAuthToken(
        @Body requset : GoogleAuthRequestDto
    ) : GoogleAuthResponseDto
}