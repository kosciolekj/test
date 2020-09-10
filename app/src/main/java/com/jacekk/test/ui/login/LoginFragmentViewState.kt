package com.jacekk.test.ui.login

import androidx.annotation.StringRes

data class LoginFragmentViewState(
    @StringRes val usernameError: Int? = null,
    @StringRes val passwordError: Int? = null,
    val isDataValid: Boolean = false
)