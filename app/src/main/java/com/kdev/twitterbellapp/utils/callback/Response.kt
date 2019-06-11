package com.kdev.twitterbellapp.utils.callback

import java.lang.Exception


sealed class Response<out T : Any> {

    sealed class Login {
        class TokenReceived(val data: Any? = null): Response<Any>()
        class TokenFailed(val data: Any? = null): Response<Any>()
    }

}