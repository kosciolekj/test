package com.jacekk.test.network

import com.jacekk.test.network.data.LoginRequest
import com.jacekk.test.network.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val BASE_URL = "https://applover-login.herokuapp.com"

interface LoginApi {

    @Headers("Content-Type: application/json")
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

}