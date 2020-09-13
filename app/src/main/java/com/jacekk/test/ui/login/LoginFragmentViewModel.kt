package com.jacekk.test.ui.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jacekk.test.R

class LoginFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginViewState = MutableLiveData<LoginFragmentViewState>(LoginFragmentViewState())
    val loginViewState: LiveData<LoginFragmentViewState> = _loginViewState


    private fun checkUsernameError(username: String): Int? {
        return with(username) {
            if (isEmpty() || isUsernameValid(this)) {
                null
            } else {
                R.string.username_error
            }
        }
    }

    // username validation check
    private fun isUsernameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun checkPasswordError(password: String): Int? {
        return with(password) {
            if (isEmpty() || isPasswordValid(this)) {
                null
            } else {
                R.string.password_error
            }
        }
    }

    // password validation check
    // assumption: password shorter then 5 characters are invalid
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }

    fun credentialsChanged(username: String, password: String) {
        val usernameError = checkUsernameError(username)
        val passwordError = checkPasswordError(password)
        val isValid = usernameError == null
                && passwordError == null
                && username.isNotEmpty()
                && password.isNotEmpty()

        _loginViewState.value = LoginFragmentViewState(usernameError, passwordError, isValid)
    }

    fun reset() {
        _loginViewState.value = LoginFragmentViewState()
    }

}