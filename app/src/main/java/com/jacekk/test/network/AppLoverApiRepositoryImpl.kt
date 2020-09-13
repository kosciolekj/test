package com.jacekk.test.network

import com.jacekk.test.network.data.LoginRequest
import com.jacekk.test.network.data.LoginResponse
import com.jacekk.test.network.data.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AppLoverApiRepositoryImpl(
    private val networkDispatcher: CoroutineDispatcher,
    private val loginApi: LoginApi
) : AppLoverApiRepository {

    override suspend fun login(username: String, password: String): Resource<LoginResponse> =
        makeCall(networkDispatcher) {
            val response = loginApi.login(LoginRequest(username, password))
            response
        }

    suspend fun <T> makeCall(
        coroutineDispatcher: CoroutineDispatcher,
        call: suspend () -> T
    ): Resource<T> = withContext(
        coroutineDispatcher
    ) {
        return@withContext try {
            val T = call()
            Resource.Success(T)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.NetworkError
                is HttpException -> Resource.HttpError(t)
                else -> Resource.GenericError(t.message)
            }
        }
    }
}