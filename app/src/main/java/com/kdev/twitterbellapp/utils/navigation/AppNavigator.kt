package com.kdev.twitterbellapp.utils.navigation

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.kdev.twitterbellapp.ui.title.TitleActivity
import com.kdev.twitterbellapp.utils.Constants.REQUEST_ENABLE_GPS

class AppNavigator(private val activity: FragmentActivity) {

    private val controller by lazy { NavigationController(activity) }

    fun navigateToGpsSettings() {
        controller.startActivityForResult(Settings.ACTION_LOCATION_SOURCE_SETTINGS, REQUEST_ENABLE_GPS)
    }

    fun navigateToTitleActivity(args: Bundle) {
        controller.startActivity(TitleActivity::class.java, args)
    }



}