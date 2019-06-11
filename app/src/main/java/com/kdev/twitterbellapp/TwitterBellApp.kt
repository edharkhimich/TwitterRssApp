package com.kdev.twitterbellapp

import android.app.Application
import android.util.Log
import com.twitter.sdk.android.core.Twitter
import timber.log.Timber
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.TwitterConfig



class TwitterBellApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
            .debug(true)
            .build()
        Twitter.initialize(config)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

}