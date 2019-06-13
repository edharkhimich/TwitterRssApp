package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Statuses {

    @SerializedName("statuses")
    @Expose
    var statuses: List<Tweet>? = null

}