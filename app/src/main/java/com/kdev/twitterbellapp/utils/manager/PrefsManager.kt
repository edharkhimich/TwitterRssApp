package com.kdev.twitterbellapp.utils.manager

import android.content.Context
import android.content.SharedPreferences
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.utils.common.SingletonHolder

class PrefsManager private constructor(context: Context) {

    val consumerKey by lazy { context.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY) }
    val consumerSecret by lazy { context.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET) }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Companion.SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun receiveString(key: String, defValue: String): String? = sharedPreferences.getString(key, defValue)

    fun removeString(key: String){
        sharedPreferences.edit().remove(key).apply()
    }



    companion object : SingletonHolder<PrefsManager, Context>(::PrefsManager) {
        const val SHARED_PREFS_NAME = "twitter_prefs"
    }
}