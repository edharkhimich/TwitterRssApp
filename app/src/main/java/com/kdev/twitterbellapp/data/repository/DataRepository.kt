package com.kdev.twitterbellapp.data.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.kdev.twitterbellapp.utils.callback.Response

interface DataRepository {

    fun getLastKnownDeviceLocation(): Location?
    fun getObservableLastKnownDeviceLocation(): MutableLiveData<Location>
    fun setLastKnownDeviceLocation(location: Location?)

    fun saveAuthData(token: String, secret: String)
    fun getAuthToken(): String?
    fun getBearerToken(): String?

    suspend fun fetchTweetsByLocation(lat: Double, long: Double): Response<Any>?
    suspend fun fetchTweetsPlaces(lat: Double, long: Double): Response<Any>?
}