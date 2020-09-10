package com.jacekk.test.network

import com.jacekk.test.network.data.LoginRequest
import com.jacekk.test.network.data.LoginResponse
import com.jacekk.test.network.data.Resource
import kotlinx.coroutines.CoroutineDispatcher

class NetworkApiRepositoryImpl(
    private val networkDispatcher: CoroutineDispatcher,
    private val networkApi: NetworkApi
) : NetworkApiRepository {
    override suspend fun login(username: String, password: String): Resource<LoginResponse> =
        makeCall(networkDispatcher) {
            val response = networkApi.login(LoginRequest(username, password))
            response
        }
}