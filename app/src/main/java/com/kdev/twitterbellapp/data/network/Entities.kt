package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Entities {

    @SerializedName("hashtags")
    @Expose
    var hashtags: List<Any>? = null
    @SerializedName("symbols")
    @Expose
    var symbols: List<Any>? = null
    @SerializedName("user_mentions")
    @Expose
    var userMentions: List<UserMention>? = null
    @SerializedName("urls")
    @Expose
    var urls: List<Any>? = null

}
