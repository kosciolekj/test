package com.jacekk.test.data

import com.jacekk.test.data.model.LoggedUser
import com.jacekk.test.network.NetworkApiRepository
import com.jacekk.test.network.data.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepositoryImpl(val networkApiRepository: NetworkApiRepository) : LoginRepository {

    var user: LoggedUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    override suspend fun login(username: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            val userResponse = networkApiRepository.login(username, password)
            if (userResponse is Resource.Success) {
                user = LoggedUser(username)
                true
            } else {
                user = null
                false
            }
        }
}