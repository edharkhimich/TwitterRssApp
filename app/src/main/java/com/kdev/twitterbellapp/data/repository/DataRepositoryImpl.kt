package com.kdev.twitterbellapp.data.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData

object DataRepositoryImpl: DataRepository {

    private val lastKnownLocation = MutableLiveData<Location>()

    override fun getLastKnownDeviceLocation(): Location? = lastKnownLocation.value

    override fun getObservableLastKnownDeviceLocation(): MutableLiveData<Location> = lastKnownLocation

    override fun setLastKnownDeviceLocation(location: Location?) {
        location?.let { lastKnownLocation.value = location }
    }

    override fun fetchTweetsByLocation() {

    }


}