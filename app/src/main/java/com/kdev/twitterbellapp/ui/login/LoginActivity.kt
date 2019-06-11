package com.kdev.twitterbellapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.utils.common.TwitterViewModelFactory
import com.kdev.twitterbellapp.utils.manager.PrefsManager
import com.kdev.twitterbellapp.utils.view.showToast
import kotlinx.android.synthetic.main.activity_login.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore

class LoginActivity : AppCompatActivity() {

    private val viewModelFactory by lazy { TwitterViewModelFactory(PrefsManager.getInstance(this)) }
    private val vm by lazy { ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setButtonCallback()
    }

    /** Using the MVVM architecture and Repository pattern for login integration we also can use TwitterAuthClient
     * calling twitterAuthClient.authorize() method where as first param we should send activity. But using
     * Repository pattern - it shouldn't know anything about views or activities. */
    private fun setButtonCallback() {
        login_button.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val authToken = session.authToken
                vm.saveAuthData(authToken.token, authToken.secret)
            }

            override fun failure(exception: TwitterException?) {
                showToast(getString(R.string.server_error))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        login_button.onActivityResult(requestCode, resultCode, data);
    }
}