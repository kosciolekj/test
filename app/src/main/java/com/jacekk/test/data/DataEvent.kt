package com.jacekk.test.data

class DataEvent<out T>(private val data: T) {

    var isDataHandled = false
        private set

    fun getDataIfNotHandled(): T? {
        return if (isDataHandled) {
            null
        } else {
            isDataHandled = true
            data
        }
    }

    fun getData(): T = data

}