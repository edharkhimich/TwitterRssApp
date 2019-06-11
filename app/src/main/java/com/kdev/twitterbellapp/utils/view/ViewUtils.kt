package com.kdev.twitterbellapp.utils.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.utils.Constants.GPS_MODE

private var toast: Toast? = null

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Activity.showToast(message: String, short: Boolean = true) {
    val tDuration: Int = if (short) LENGTH_SHORT
    else LENGTH_LONG

    if (toast != null) toast?.cancel()

    toast = Toast.makeText(this, message, tDuration).apply {
        show()
    }
}

fun Activity.showDialog(
    mode: Byte, title: String?, message: String?, pTitle: String, pClick: () -> Unit?,
    nClick: () -> Unit? = { null }, nTitle: String = resources.getString(R.string.cancel), singleButton: Boolean = false
) {
    val builder = AlertDialog.Builder(this)
    builder.apply {
        setTitle(title)
        setMessage(message)

        if (!singleButton) {
            setPositiveButton(pTitle) { dialog, which -> pClick() }
            setNegativeButton(nTitle) { dialog, which ->
                if (nClick() == null) {
                    val error = when(mode){
                        GPS_MODE -> resources.getString(R.string.location_disabled)
                        else -> resources.getString(R.string.settings_disabled)
                    }
                    dialog.cancel()
                    showToast(error)
                }
                else nClick()
            }
        } else setPositiveButton(pTitle) { dialog, which -> dialog.cancel() }

        create()
        show()
    }
}