package com.jacekk.test.data

interface LoginRepository {

    suspend fun login(username: String, password: String): Boolean

}