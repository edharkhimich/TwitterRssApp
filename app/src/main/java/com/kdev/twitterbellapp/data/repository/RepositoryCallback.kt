package com.kdev.twitterbellapp.data.repository

interface RepositoryCallback<T> {
    fun onSuccess(`object`: T)
    fun onFailure(error: Throwable)
}