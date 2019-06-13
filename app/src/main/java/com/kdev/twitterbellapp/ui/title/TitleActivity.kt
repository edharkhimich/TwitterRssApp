package com.kdev.twitterbellapp.ui.title

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View.GONE
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.kdev.twitterbellapp.R
import com.google.android.gms.maps.SupportMapFragment
import com.kdev.twitterbellapp.ui.base.BaseActivity
import com.kdev.twitterbellapp.utils.Constants.GUEST_MODE
import com.kdev.twitterbellapp.utils.Constants.LOCATION_KEY
import com.kdev.twitterbellapp.utils.Constants.MODE_KEY
import com.kdev.twitterbellapp.utils.view.showToast
import kotlinx.android.synthetic.main.activity_title.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.kdev.twitterbellapp.utils.Constants.ZOOM_VALUE


class TitleActivity: BaseActivity<TitleViewModel> (TitleViewModel::class.java), OnMapReadyCallback {

    private val mapFragment by lazy { supportFragmentManager
        .findFragmentById(R.id.title_map) as SupportMapFragment? }

    private val mode by lazy { intent?.getByteExtra(MODE_KEY, GUEST_MODE) }
    private var map: GoogleMap? = null

    override fun getLayoutId(): Int = R.layout.activity_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            init()
            if(mode == GUEST_MODE) showToast(resources.getString(R.string.your_loged_in_as_guest))
        }
    }

    private fun init(){
        if(isInternetConnected()) {
            mapFragment?.getMapAsync(this)
        } else showToast(getString(R.string.no_internet_connection))
        title_progress_bar.visibility = GONE
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        checkLocation()
    }

    override fun observeViewModel() {
        vm?.let { _vm ->
            _vm.getObservableLastKnownDeviceLocation().observe(this, Observer { location ->
                if (location == null) {
                    showToast(getString(R.string.failed_to_get_device_location))
                } else bindMap(location)
            })
        }
    }

    @SuppressLint("MissingPermission")
    private fun animateMap(location: Location){
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VALUE)

        map?.animateCamera(cameraUpdate)
        map?.isMyLocationEnabled = true
    }

    private fun checkLocation(){
        if(vm?.getLastKnownDeviceLocation() == null) {
            requestLastKnownLocation()
        } else {
            vm?.getLastKnownDeviceLocation()?.let { location ->
                bindMap(location)
            }
        }
    }

    private fun bindMap(location: Location){
        vm?.fetchTweetsByLocation(location.latitude.toDouble(), location.longitude.toDouble())
//        vm?.fetchByQuery(consumerKey, consumerSecret,"fa")
        animateMap(location)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LOCATION_KEY, vm?.getLastKnownDeviceLocation())
    }
}
