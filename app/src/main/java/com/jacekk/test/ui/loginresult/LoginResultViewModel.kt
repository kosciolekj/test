package com.jacekk.test.ui.loginresult

import android.app.Application
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jacekk.test.R
import com.jacekk.test.TestApp
import com.jacekk.test.data.DataEvent
import com.jacekk.test.network.AppLoverApiRepository
import com.jacekk.test.network.data.Resource
import kotlinx.coroutines.launch


class LoginResultViewModel(
    application: Application,
    private val appLoverApiRepository: AppLoverApiRepository
) :
    AndroidViewModel(application) {

    private val _loadingVisibility = MutableLiveData<Int>(View.VISIBLE)
    val loadingVisibility: LiveData<Int> = _loadingVisibility

    private val _resultVisibility = MutableLiveData<Int>(View.GONE)
    val resultVisibility: LiveData<Int> = _resultVisibility

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _error = MutableLiveData<DataEvent<String>>()
    val error: LiveData<DataEvent<String>> = _error

    fun init(username: String, password: String) {
        viewModelScope.launch {
            val apiResponse = appLoverApiRepository.login(username, password)
            if (apiResponse is Resource.Success) {
                _loadingVisibility.value = View.GONE
                _result.value =
                    getApplication<TestApp>().resources.getString(R.string.login_success)
                _resultVisibility.value = View.VISIBLE
            } else {
                _loadingVisibility.value = View.GONE
                _result.value = getApplication<TestApp>().resources.getString(R.string.login_failed)
                _resultVisibility.value = View.VISIBLE
                when (apiResponse) {
                    is Resource.NetworkError -> onNetworkError()
                    is Resource.HttpError -> onHttpError(apiResponse)
                    is Resource.GenericError -> onGenericError(apiResponse.message)
                }
            }
        }
    }

    private fun onGenericError(message: String?) {
        message?.let {
            _error.value = DataEvent("Error: %s".format(it))
        }
    }

    private fun onNetworkError() {
        _error.value =
            DataEvent(getApplication<TestApp>().resources.getString(R.string.error_network))
    }

    private fun onHttpError(apiResponse: Resource.HttpError) {
        val code: Int = apiResponse.exception.code()
        var httpError: DataEvent<String>? = when (code) {
            400 -> DataEvent(getString(R.string.error_400))
            422 -> DataEvent(getString(R.string.error_422))
            500 -> DataEvent(getString(R.string.error_500))
            else -> null
        }
        httpError?.let {
            _error.value = it
        }
    }

    private fun getString(@StringRes stringResId: Int): String {
        return getApplication<TestApp>().resources.getString(stringResId)
    }
}