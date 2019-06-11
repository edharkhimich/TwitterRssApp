package com.kdev.twitterbellapp.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.utils.Constants.GPS_MODE
import com.kdev.twitterbellapp.utils.Constants.LOCATION_KEY
import com.kdev.twitterbellapp.utils.Constants.PERMISSIONS_REQUEST_COARSE_LOCATION
import com.kdev.twitterbellapp.utils.Constants.REQUEST_CHECK_SETTINGS
import com.kdev.twitterbellapp.utils.Constants.REQUEST_ENABLE_GPS
import com.kdev.twitterbellapp.utils.navigation.AppNavigator
import com.kdev.twitterbellapp.utils.network.isNetworkEnable
import com.kdev.twitterbellapp.utils.view.showDialog
import com.kdev.twitterbellapp.utils.view.showToast
import timber.log.Timber

abstract class BaseActivity<VM : BaseViewModel>(private val viewModelClass: Class<VM>) : AppCompatActivity() {

    protected var vm: VM? = null
    protected var savedLocation: Location? = null
    protected val appNavigator by lazy { AppNavigator(this) }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var locationRequest: LocationRequest? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            setContentView(getLayoutId())
            vm = getViewModeld()

            observeViewModel()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        } else savedLocation = savedInstanceState.getParcelable(LOCATION_KEY)
    }

    fun isDeviceReadyToGetLocation() = isLocationPermissionGranted() && isGpsEnabled()

    fun requestLastKnownLocation() {
        Timber.d("requestLastKnownLocation()")
        if (isLocationPermissionGranted() && isGpsEnabled()) {
            Timber.d("isLocationPermissionGranted() && isGpsEnabled()")
            retrieveLastKnownLocation()
        } else {
            Timber.d("isLocationPermissionGranted() && isGpsEnabled() == false")
            checkLocationPermission()
        }
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun isGpsEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            showDialog(
                GPS_MODE,
                resources.getString(R.string.enable_location),
                resources.getString(R.string.enable_location_description),
                resources.getString(R.string.enable), { checkGpsSettings()})
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_COARSE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_COARSE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkGpsSettings()
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showToast(getString(R.string.failed_to_get_device_location))
                }
            }
        }
    }

    private fun checkGpsSettings() {
        if (isGpsEnabled())
            retrieveLastKnownLocation()
        else
            appNavigator.navigateToGpsSettings()
    }

    @SuppressLint("MissingPermission")
    private fun retrieveLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Timber.d("location $location")
            if (location == null) {
                checkCurrentLocationSettings()
            } else {
                vm?.setLastKnownDeviceLocation(location)
            }
        }
    }

    private fun checkCurrentLocationSettings() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (locationRequest == null) {
            showToast(getString(R.string.failed_to_get_device_location))
            return
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { requestLocationUpdates() }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    showToast(getString(R.string.failed_to_get_device_location))
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        Timber.d("requestLocationUpdates")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    showToast(getString(R.string.failed_to_get_device_location))
                    return
                }
                vm?.setLastKnownDeviceLocation(locationResult.lastLocation)
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult ${requestCode}")

        when (requestCode) {
            REQUEST_ENABLE_GPS -> {
                if (isGpsEnabled())
                    retrieveLastKnownLocation()
                else
                    showToast(getString(R.string.failed_to_get_device_location))
            }
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK)
                    requestLocationUpdates()
                else
                    showToast(getString(R.string.failed_to_get_device_location))
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    open fun observeViewModel() {}

    open fun getViewModeld(): VM {
        return ViewModelProviders.of(this).get(viewModelClass)
    }

    protected fun isInternetConnected(): Boolean {
        return if (isNetworkEnable(this)) {
            true
        } else {
            showToast(getString(R.string.no_internet_connection))
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LOCATION_KEY, vm?.getLastKnownDeviceLocation())
    }
}