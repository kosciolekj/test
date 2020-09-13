package com.jacekk.test.network.data

import retrofit2.HttpException

sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class HttpError(val exception: HttpException) : Resource<Nothing>()
    data class GenericError(val message: String?) : Resource<Nothing>()
    object NetworkError : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is HttpError -> "HttpError[exception=$exception]"
            is GenericError -> "GenericError[message=$message]"
            is NetworkError -> "NetworkError"
        }
    }
}