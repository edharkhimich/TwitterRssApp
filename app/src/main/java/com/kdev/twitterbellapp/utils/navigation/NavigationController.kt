package com.kdev.twitterbellapp.utils.navigation

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class NavigationController(private val activity: FragmentActivity) : Navigator {

    override fun finishActivity() {
        activity.finish()
    }

    override fun startActivityForResult(action: String, requestCode: Int) {
        activity.startActivityForResult(Intent(action), requestCode)
    }

    override fun startActivity(activityClass: Class<out Activity>, args: Bundle?, requestCode: Int?, newTask: Boolean) {

        val intent = Intent(activity, activityClass)

        if (args != null) intent.putExtras(args)

        if (newTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        }

        if (requestCode != null) activity.startActivityForResult(intent, requestCode)
        else activity.startActivity(intent)
    }

}