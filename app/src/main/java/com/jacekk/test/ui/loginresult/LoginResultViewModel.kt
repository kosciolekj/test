package com.jacekk.test.ui.loginresult

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jacekk.test.data.LoginRepository


class LoginResultViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loadingVisibility = MutableLiveData<Int>(View.VISIBLE)
    val loadingVisibility: LiveData<Int> = _loadingVisibility

    private val _resultVisibility = MutableLiveData<Int>(View.GONE)
    val resultVisibility: LiveData<Int> = _resultVisibility

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

}