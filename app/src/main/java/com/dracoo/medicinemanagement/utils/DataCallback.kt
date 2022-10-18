package com.dracoo.medicinemanagement.utils

interface DataCallback<T> {
    fun onDataLoaded(data: T?)
    fun onDataError(error: String?)
}