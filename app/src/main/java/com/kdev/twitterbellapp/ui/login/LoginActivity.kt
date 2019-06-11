package com.kdev.twitterbellapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.ui.base.BaseActivity
import com.kdev.twitterbellapp.utils.Constants.GUEST_MODE
import com.kdev.twitterbellapp.utils.Constants.MODE_KEY
import com.kdev.twitterbellapp.utils.Constants.USER_MODE
import com.kdev.twitterbellapp.utils.callback.Response
import com.twitter.sdk.android.core.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class.java) {

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm?.checkToken()
    }

    override fun observeViewModel() {
        vm?.getResponse()?.observe(this, Observer {
            when (it) {
                is Response.Login.TokenReceived -> navigator.navigateToTitleActivity(Bundle().apply {
                    putByte(MODE_KEY, USER_MODE)
                })
                is Response.Login.TokenFailed -> setButtonCallback()
            }
        })
    }

    /** Using the MVVM architecture and Repository pattern for login integration we also can use TwitterAuthClient
     * calling twitterAuthClient.authorize() method where as first param we should send activity. But using
     * Repository pattern - it shouldn't know anything about views or activities. */
    private fun setButtonCallback() {
        login_button.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val authToken = session.authToken

                vm?.saveAuthData(authToken.token, authToken.secret)
                navigator.navigateToTitleActivity(Bundle().apply { putByte(MODE_KEY, USER_MODE) })
            }

            override fun failure(exception: TwitterException?) {
                navigator.navigateToTitleActivity(Bundle().apply { putByte(MODE_KEY, GUEST_MODE) })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        login_button.onActivityResult(requestCode, resultCode, data)
    }
}