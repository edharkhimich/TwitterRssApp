package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserMention {

    @SerializedName("screen_name")
    @Expose
    var screenName: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("id_str")
    @Expose
    var idStr: String? = null
    @SerializedName("indices")
    @Expose
    var indices: List<Int>? = null

}
