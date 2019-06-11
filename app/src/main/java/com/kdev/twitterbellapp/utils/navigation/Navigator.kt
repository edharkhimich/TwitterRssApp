package com.kdev.twitterbellapp.utils.navigation

import android.app.Activity
import android.os.Bundle

interface Navigator {

    fun finishActivity()

    fun startActivity(activityClass: Class<out Activity>, args: Bundle?, requestCode: Int?, newTask: Boolean = false)

    fun startActivityForResult(action: String, requestCode: Int)

}