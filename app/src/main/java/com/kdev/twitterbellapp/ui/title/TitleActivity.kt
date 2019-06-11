package com.kdev.twitterbellapp.ui.title

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.kdev.twitterbellapp.R
import com.google.android.gms.maps.SupportMapFragment
import com.kdev.twitterbellapp.ui.base.BaseActivity
import com.kdev.twitterbellapp.utils.Constants.LOCATION_KEY
import com.kdev.twitterbellapp.utils.view.showToast
import timber.log.Timber


class TitleActivity: BaseActivity<TitleViewModel> (TitleViewModel::class.java), OnMapReadyCallback {

    private val mapFragment by lazy { supportFragmentManager
        .findFragmentById(R.id.title_map) as SupportMapFragment? }

    override fun getLayoutId(): Int = R.layout.activity_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            init()
        } else savedLocation
    }

    override fun onResume() {
        super.onResume()
        if(vm?.getLastKnownDeviceLocation() == null){

        }
    }

    private fun init(){
        if(isInternetConnected()) {
            mapFragment?.getMapAsync(this)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        Timber.d("onMapReady")
        checkLocation()
    }

    override fun observeViewModel() {
        vm?.let { _vm ->
            _vm.getObservableLastKnownDeviceLocation().observe(this, Observer { location ->
                if (location == null) {
                    Timber.d("location == null")
                    showToast("Np location")
                } else {
                    Timber.d("location != null -> ${location.latitude} / ${location.longitude}")
                    _vm.fetchData(location)
                }
            })
        }
    }

    private fun checkLocation(){
        if(vm?.getLastKnownDeviceLocation() == null) {
            requestLastKnownLocation()
        } else {
            Timber.d("location is not null ${vm?.getLastKnownDeviceLocation()}" )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LOCATION_KEY, vm?.getLastKnownDeviceLocation())
    }


}
