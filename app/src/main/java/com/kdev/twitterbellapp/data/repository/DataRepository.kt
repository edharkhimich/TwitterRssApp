package com.kdev.twitterbellapp.data.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData

interface DataRepository {

    fun getLastKnownDeviceLocation(): Location?
    fun getObservableLastKnownDeviceLocation(): MutableLiveData<Location>
    fun setLastKnownDeviceLocation(location: Location?)

    fun saveAuthData(token: String, secret: String)
    fun getToken(): String?

    fun fetchTweetsByLocation()
}