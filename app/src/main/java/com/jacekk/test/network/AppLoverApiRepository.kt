package com.jacekk.test.network

import com.jacekk.test.network.data.LoginResponse
import com.jacekk.test.network.data.Resource

interface AppLoverApiRepository {

    suspend fun login(username: String, password: String): Resource<LoginResponse>

}


