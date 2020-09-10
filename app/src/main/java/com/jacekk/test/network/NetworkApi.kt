package com.jacekk.test.network

import com.jacekk.test.network.data.LoginRequest
import com.jacekk.test.network.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkApi {

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

}