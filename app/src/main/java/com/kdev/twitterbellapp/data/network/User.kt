package com.kdev.twitterbellapp.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("id_str")
    @Expose
    var idStr: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("screen_name")
    @Expose
    var screenName: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("url")
    @Expose
    var url: Any? = null
    @SerializedName("entities")
    @Expose
    var entities: Entities_? = null
    @SerializedName("protected")
    @Expose
    var protected: Boolean? = null
    @SerializedName("followers_count")
    @Expose
    var followersCount: Int? = null
    @SerializedName("friends_count")
    @Expose
    var friendsCount: Int? = null
    @SerializedName("listed_count")
    @Expose
    var listedCount: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("favourites_count")
    @Expose
    var favouritesCount: Int? = null
    @SerializedName("utc_offset")
    @Expose
    var utcOffset: Any? = null
    @SerializedName("time_zone")
    @Expose
    var timeZone: Any? = null
    @SerializedName("geo_enabled")
    @Expose
    var geoEnabled: Boolean? = null
    @SerializedName("verified")
    @Expose
    var verified: Boolean? = null
    @SerializedName("statuses_count")
    @Expose
    var statusesCount: Int? = null
    @SerializedName("lang")
    @Expose
    var lang: String? = null
    @SerializedName("contributors_enabled")
    @Expose
    var contributorsEnabled: Boolean? = null
    @SerializedName("is_translator")
    @Expose
    var isTranslator: Boolean? = null
    @SerializedName("is_translation_enabled")
    @Expose
    var isTranslationEnabled: Boolean? = null
    @SerializedName("profile_background_color")
    @Expose
    var profileBackgroundColor: String? = null
    @SerializedName("profile_background_image_url")
    @Expose
    var profileBackgroundImageUrl: String? = null
    @SerializedName("profile_background_image_url_https")
    @Expose
    var profileBackgroundImageUrlHttps: String? = null
    @SerializedName("profile_background_tile")
    @Expose
    var profileBackgroundTile: Boolean? = null
    @SerializedName("profile_image_url")
    @Expose
    var profileImageUrl: String? = null
    @SerializedName("profile_image_url_https")
    @Expose
    var profileImageUrlHttps: String? = null
    @SerializedName("profile_banner_url")
    @Expose
    var profileBannerUrl: String? = null
    @SerializedName("profile_link_color")
    @Expose
    var profileLinkColor: String? = null
    @SerializedName("profile_sidebar_border_color")
    @Expose
    var profileSidebarBorderColor: String? = null
    @SerializedName("profile_sidebar_fill_color")
    @Expose
    var profileSidebarFillColor: String? = null
    @SerializedName("profile_text_color")
    @Expose
    var profileTextColor: String? = null
    @SerializedName("profile_use_background_image")
    @Expose
    var profileUseBackgroundImage: Boolean? = null
    @SerializedName("has_extended_profile")
    @Expose
    var hasExtendedProfile: Boolean? = null
    @SerializedName("default_profile")
    @Expose
    var defaultProfile: Boolean? = null
    @SerializedName("default_profile_image")
    @Expose
    var defaultProfileImage: Boolean? = null
    @SerializedName("following")
    @Expose
    var following: Any? = null
    @SerializedName("follow_request_sent")
    @Expose
    var followRequestSent: Any? = null
    @SerializedName("notifications")
    @Expose
    var notifications: Any? = null
    @SerializedName("translator_type")
    @Expose
    var translatorType: String? = null

}
