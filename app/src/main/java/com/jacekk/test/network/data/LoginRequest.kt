package com.jacekk.test.network.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @Expose(serialize = true)
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    @Expose(serialize = true)
    val password: String
)