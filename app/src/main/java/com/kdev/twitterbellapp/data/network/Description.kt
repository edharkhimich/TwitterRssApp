package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Description {

    @SerializedName("urls")
    @Expose
    var urls: List<Any>? = null

}
