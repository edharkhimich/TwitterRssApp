package com.kdev.twitterbellapp.utils.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kdev.twitterbellapp.ui.login.LoginViewModel
import com.kdev.twitterbellapp.ui.title.TitleViewModel
import com.kdev.twitterbellapp.utils.manager.PrefsManager

class TwitterViewModelFactory(private val prefsManager: PrefsManager) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            LoginViewModel::class.java -> LoginViewModel(prefsManager) as T
            else -> TitleViewModel(prefsManager) as T
        }
    }
}