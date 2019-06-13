package com.kdev.twitterbellapp.utils.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.utils.Constants.GPS_MODE
import com.kdev.twitterbellapp.utils.Constants.GUEST_MODE

private var toast: Toast? = null

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
                    val error = when (mode) {
                        GPS_MODE -> resources.getString(R.string.location_disabled)
                        GUEST_MODE -> resources.getString(R.string.login_failure)
                        else -> resources.getString(R.string.settings_disabled)
                    }
                    dialog.cancel()
                    showToast(error)
                } else nClick()
            }
        } else setPositiveButton(pTitle) { dialog, which -> dialog.cancel() }

        create()
        show()
    }
}

fun Context.getSpannableText(text: String): Spannable {
    val tags = hashMapOf<Int, Int>()
    var state = false
    var startIndex = 0
    var endIndex = 0
    if (text.contains('@')) {
        for (pos in 0 until text.length) {
            if (text[pos] == '@') {
                state = true
                startIndex = pos
            } else if (state && text[pos] == ' ') {
                endIndex = pos
                tags[startIndex] = endIndex
                state = false
            }
        }
    }

    val spannable = SpannableString(text)

    tags.forEach { entry ->
        startIndex = entry.key
        endIndex = entry.value
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}