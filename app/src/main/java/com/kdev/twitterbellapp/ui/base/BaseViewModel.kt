package com.kdev.twitterbellapp.ui.base

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdev.twitterbellapp.data.repository.DataRepositoryImpl
import com.kdev.twitterbellapp.utils.callback.Response

abstract class BaseViewModel : ViewModel() {

    protected val dataRepository by lazy { DataRepositoryImpl }

    protected val response = MutableLiveData<Response<Any>>()

    fun getLastKnownDeviceLocation() = dataRepository.getLastKnownDeviceLocation()

    fun getObservableLastKnownDeviceLocation() = dataRepository.getObservableLastKnownDeviceLocation()

    fun setLastKnownDeviceLocation(location: Location?) = dataRepository.setLastKnownDeviceLocation(location)

    fun getResponse(): LiveData<Response<Any>> = response


}