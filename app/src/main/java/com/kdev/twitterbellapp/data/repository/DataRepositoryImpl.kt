package com.kdev.twitterbellapp.data.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.kdev.twitterbellapp.utils.Constants.DEF
import com.kdev.twitterbellapp.utils.Constants.SECRET_KEY
import com.kdev.twitterbellapp.utils.Constants.TOKEN_KEY
import com.kdev.twitterbellapp.utils.common.SingletonHolder
import com.kdev.twitterbellapp.utils.manager.PrefsManager

class DataRepositoryImpl constructor(private val prefs: PrefsManager): DataRepository {

    private val lastKnownLocation = MutableLiveData<Location>()

    override fun getLastKnownDeviceLocation(): Location? = lastKnownLocation.value

    override fun getObservableLastKnownDeviceLocation(): MutableLiveData<Location> = lastKnownLocation

    override fun setLastKnownDeviceLocation(location: Location?) {
        location?.let { lastKnownLocation.value = location }
    }

    override fun fetchTweetsByLocation() {

    }

    override fun saveAuthData(token: String, secret: String) {
        prefs.run {
            saveString(TOKEN_KEY, token)
            saveString(SECRET_KEY, secret)
        }
    }

    override fun getToken(): String? = prefs.receiveString(TOKEN_KEY, DEF)

    companion object : SingletonHolder<DataRepositoryImpl, PrefsManager>(::DataRepositoryImpl)
}