package com.dracoo.medicinemanagement.utils

interface DataCallback<T> {
    fun onDataLoaded(data: T?)
    fun onDataError(error: String?)
}

interface StraightApiCallBack {
    fun onDataLoaded(data: String?)
    fun onDataError(error: String?)
}