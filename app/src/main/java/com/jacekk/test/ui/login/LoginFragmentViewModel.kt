package com.jacekk.test.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jacekk.test.R

class LoginFragmentViewModel : ViewModel() {

    private val _loginViewState = MutableLiveData<LoginFragmentViewState>(LoginFragmentViewState())
    val loginViewState: LiveData<LoginFragmentViewState> = _loginViewState

    // username validation check
    private fun isUsernameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // password validation check
    // assumption: password shorter then 5 characters are invalid
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }

    fun credentialsChanged(username: String, password: String) {
        val usernameError = if (!isUsernameValid(username)) R.string.username_error else null
        val passwordError = if (!isPasswordValid(password)) R.string.password_error else null
        val isValid = usernameError == null && passwordError == null

        _loginViewState.value = LoginFragmentViewState(usernameError, passwordError, isValid)
    }

    fun reset() {
        _loginViewState.value = LoginFragmentViewState()
    }

}