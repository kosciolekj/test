package com.jacekk.test.network

import com.jacekk.test.network.data.LoginResponse
import com.jacekk.test.network.data.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.ext.getOrCreateScope
import timber.log.Timber
import java.lang.Exception

interface NetworkApiRepository {

    suspend fun login(username: String, password: String): Resource<LoginResponse>

    suspend fun <T> makeCall(coroutineDispatcher: CoroutineDispatcher, call: suspend () -> T): Resource<T> = withContext(
        coroutineDispatcher
    ) {
        return@withContext try {
            val T = call()
            Resource.Success(T)
        } catch (e: Exception) {
            Timber.e(e, "Exception when handling with error: $e")
            Resource.Error(e)
        }
    }

}


