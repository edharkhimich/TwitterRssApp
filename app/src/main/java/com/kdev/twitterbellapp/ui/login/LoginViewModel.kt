package com.kdev.twitterbellapp.ui.login

import com.kdev.twitterbellapp.ui.base.BaseViewModel
import com.kdev.twitterbellapp.utils.Constants.DEF
import com.kdev.twitterbellapp.utils.callback.Response
import com.kdev.twitterbellapp.utils.manager.PrefsManager

class LoginViewModel(prefsManager: PrefsManager) : BaseViewModel(prefsManager) {

    fun saveAuthData(token: String, secret: String) {
        dataRepository.saveAuthData(token, secret)
    }

    /** Check if the user is already logged in */
    fun checkToken() {
        val result = dataRepository.getToken()

        response.value = if(result != null && result != DEF)
            Response.Login.TokenReceived()
        else Response.Login.TokenFailed()
    }
}