package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tweet {

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("id_str")
    @Expose
    var idStr: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("truncated")
    @Expose
    var truncated: Boolean? = null
    @SerializedName("entities")
    @Expose
    var entities: Entities? = null
    @SerializedName("metadata")
    @Expose
    var metadata: Metadata? = null
    @SerializedName("source")
    @Expose
    var source: String? = null
    @SerializedName("in_reply_to_status_id")
    @Expose
    var inReplyToStatusId: Int? = null
    @SerializedName("in_reply_to_status_id_str")
    @Expose
    var inReplyToStatusIdStr: String? = null
    @SerializedName("in_reply_to_user_id")
    @Expose
    var inReplyToUserId: Int? = null
    @SerializedName("in_reply_to_user_id_str")
    @Expose
    var inReplyToUserIdStr: String? = null
    @SerializedName("in_reply_to_screen_name")
    @Expose
    var inReplyToScreenName: String? = null
    @SerializedName("user")
    @Expose
    var user: User? = null
    @SerializedName("geo")
    @Expose
    var geo: Any? = null
    @SerializedName("coordinates")
    @Expose
    var coordinates: Any? = null
    @SerializedName("place")
    @Expose
    var place: Any? = null
    @SerializedName("contributors")
    @Expose
    var contributors: Any? = null
    @SerializedName("is_quote_status")
    @Expose
    var isQuoteStatus: Boolean? = null
    @SerializedName("retweet_count")
    @Expose
    var retweetCount: Int? = null
    @SerializedName("favorite_count")
    @Expose
    var favoriteCount: Int? = null
    @SerializedName("favorited")
    @Expose
    var favorited: Boolean? = null
    @SerializedName("retweeted")
    @Expose
    var retweeted: Boolean? = null
    @SerializedName("lang")
    @Expose
    var lang: String? = null

}
