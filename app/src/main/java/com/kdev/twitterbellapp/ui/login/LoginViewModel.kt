package com.kdev.twitterbellapp.ui.login

import com.kdev.twitterbellapp.ui.base.BaseViewModel
import com.kdev.twitterbellapp.utils.manager.PrefsManager

class LoginViewModel(prefsManager: PrefsManager) : BaseViewModel(prefsManager) {

    fun saveAuthData(token: String, secret: String) {
        dataRepository.saveAuthData(token, secret)
    }
}