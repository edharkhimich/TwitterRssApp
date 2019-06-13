package com.kdev.twitterbellapp.ui.title

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.ui.base.BaseActivity
import com.kdev.twitterbellapp.utils.Constants.GUEST_MODE
import com.kdev.twitterbellapp.utils.Constants.LOCATION_KEY
import com.kdev.twitterbellapp.utils.Constants.MODE_KEY
import com.kdev.twitterbellapp.utils.Constants.ZOOM_VALUE
import com.kdev.twitterbellapp.utils.view.showToast
import kotlinx.android.synthetic.main.activity_title.*
import android.view.Menu
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.kdev.twitterbellapp.data.model.local.SearchTweet
import com.kdev.twitterbellapp.utils.callback.Response


class TitleActivity : BaseActivity<TitleViewModel>(TitleViewModel::class.java), OnMapReadyCallback,
    SearchView.OnQueryTextListener {

    private var searchView: SearchView? = null

    private val mapFragment by lazy {
        supportFragmentManager
            .findFragmentById(R.id.title_map) as SupportMapFragment?
    }

    private val adapter by lazy { TitleAdapter() }

    private val mode by lazy { intent?.getByteExtra(MODE_KEY, GUEST_MODE) }
    private var map: GoogleMap? = null

    override fun getLayoutId(): Int = R.layout.activity_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            init()
            if (mode == GUEST_MODE) showToast(resources.getString(R.string.your_loged_in_as_guest))
        } else vm?.setLastKnownDeviceLocation(savedInstanceState.getParcelable(LOCATION_KEY))
    }

    private fun init() {
        if (isInternetConnected()) {
            vm?.generateBearer()
            setUpToolbar()
            bindRecView()
            mapFragment?.getMapAsync(this)
        } else showToast(getString(R.string.no_internet_connection))
    }

    private fun bindRecView(){
        title_toolbar_search_rec_view.run {
            layoutManager = LinearLayoutManager(this@TitleActivity)
            setHasFixedSize(true)
            adapter = this@TitleActivity.adapter
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(title_toolbar)
        supportActionBar?.title = null
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.uiSettings?.isMyLocationButtonEnabled = false
        checkLocation()
    }

    override fun observeViewModel() {
        vm?.let { _vm ->
            _vm.getResponse().observe(this, Observer {
                when(it){
                    is Response.Search.TweetByQueryReceived -> handleTweets(it.data)
                    is Response.Search.TweetFailure -> showToast(getString(R.string.server_error))
                    is Response.Login.TokenFailed -> showToast(getString(R.string.server_error))
                    is Response.Login.TokenReceived -> showToast(getString(R.string.token_generated))
                }
                title_progress_bar.visibility = GONE
            })
            _vm.getObservableLastKnownDeviceLocation().observe(this, Observer { location ->
                if (location == null) {
                    showToast(getString(R.string.failed_to_get_device_location))
                } else bindMap(location)
            })
        }
    }

    private fun handleTweets(list: List<SearchTweet>){
        adapter.setItems(list)
        searchView?.isEnabled = true
        title_progress_bar.visibility = GONE
    }

    @SuppressLint("MissingPermission")
    private fun animateMap(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VALUE)

        map?.animateCamera(cameraUpdate)
        map?.isMyLocationEnabled = true
    }

    private fun checkLocation() {
        if (vm?.getLastKnownDeviceLocation() == null) {
            requestLastKnownLocation()
        } else {
            vm?.getLastKnownDeviceLocation()?.let { location ->
                bindMap(location)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        if(isInternetConnected()) {
            searchView?.isEnabled = false
            if (!newText.isNullOrEmpty()) {
                title_progress_bar.visibility = VISIBLE
                vm?.fetchByQuery(newText)
            } else adapter.removeItems()
        } else showToast(getString(R.string.no_internet_connection))
        return false
    }

    private fun bindMap(location: Location) {
        /** This method receiving tweets around 5 km. Uncomment when I get user-context auth
         * and send make another request so I can compare some information from both requests */
//        vm?.fetchTweetsByLocation(location.latitude, location.longitude)

        animateMap(location)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LOCATION_KEY, vm?.getLastKnownDeviceLocation())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)
        val mSearchMenuItem = menu.findItem(R.id.menu_toolbar_search)
        searchView = mSearchMenuItem.actionView as SearchView
        searchView?.run {
            queryHint = getString(R.string.enter_text)
            setOnQueryTextListener(this@TitleActivity)
            isIconified = false
        }
        return true
    }
}
