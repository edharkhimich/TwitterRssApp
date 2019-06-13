package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Metadata {

    @SerializedName("iso_language_code")
    @Expose
    var isoLanguageCode: String? = null
    @SerializedName("result_type")
    @Expose
    var resultType: String? = null

}
