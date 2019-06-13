package com.kdev.twitterbellapp.utils.common

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kdev.twitterbellapp.utils.manager.PrefsManager


class GsonUtils() {

    var gson: Gson? = null

    init {
        gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .enableComplexMapKeySerialization()
            .create()
        gson = Gson()
    }

    fun toJson(`object`: Any): String {
        return gson!!.toJson(`object`)
    }

    companion object : SingletonSingleHolder<GsonUtils>(::GsonUtils)
}