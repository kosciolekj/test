package com.jacekk.test.network.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("status")
    val status: String?,

    @Expose
    @SerializedName("error")
    val error: String?
)