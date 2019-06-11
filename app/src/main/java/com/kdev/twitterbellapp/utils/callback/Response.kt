package com.kdev.twitterbellapp.utils.callback

import java.lang.Exception

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Response<out T : Any> {

    data class Success<out T : Any>(val data: T?) : Response<T>()

    data class Error(val exception: Exception, val errorMessage: String) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}