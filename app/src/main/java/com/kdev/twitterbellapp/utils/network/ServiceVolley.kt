package com.kdev.twitterbellapp.utils.network

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.kdev.twitterbellapp.TwitterBellApp
import com.kdev.twitterbellapp.data.repository.DataRepositoryImpl.Companion.BEARER
import com.kdev.twitterbellapp.utils.Constants.DEF
import com.twitter.sdk.android.core.internal.scribe.ScribeConfig.BASE_URL
import org.json.JSONObject

class ServiceVolley : ServiceInterface {

    private lateinit var url: String

    override suspend fun getOAuthToken(basicAuthHeader: String?, completionHandler: (response: JSONObject?) -> Unit) {
        url = BASE_URL + OAUTH2 + GRANT_TYPE + CLIENT_CREDENTIALS
        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            null,
            Response.Listener<JSONObject> { response -> completionHandler(response) },
            Response.ErrorListener { completionHandler(null)}
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers[CONTENT_TYPE] = APPLICATION_XXX_FORM
                headers[AUTHORIZATION] = basicAuthHeader ?: DEF
                return headers
            }
        }

        TwitterBellApp.instance?.addToRequestQueue(request)
    }

    override suspend fun getTweetsByLocation(
        accessToken: String?,
        lat: Double,
        long: Double,
        completionHandler: (response: JSONObject?) -> Unit,
        radius: Int
    ) {
        url = "$BASE_URL$SEARCH$GEOCODE$lat,$long,$radius$KM$AND$RESULT_TYPE$RECENT"
        val jsonObjReq = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener<JSONObject> { response -> completionHandler.invoke(response) },
            Response.ErrorListener { completionHandler.invoke(null) }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers[CONTENT_TYPE] = APPLICATION_JSON
                headers[AUTHORIZATION] = BEARER + accessToken
                return headers
            }
        }

        TwitterBellApp.instance?.addToRequestQueue(jsonObjReq)
    }

    override suspend fun getTweetsByQuery(accessToken: String?, query: String, completionHandler: (response: JSONObject?) -> Unit) {
        url = "$BASE_URL$SEARCH$QUERY$query"
        val jsonObjReq = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener<JSONObject> { response -> completionHandler.invoke(response) },
            Response.ErrorListener { completionHandler.invoke(null) }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers[CONTENT_TYPE] = APPLICATION_JSON
                headers[AUTHORIZATION] = BEARER + accessToken
                return headers
            }
        }

        TwitterBellApp.instance?.addToRequestQueue(jsonObjReq)
    }

    override suspend fun getTweetsPlaces(
        basicAuthHeader: String?,
        lat: Double,
        long: Double,
        completionHandler: (response: JSONObject?) -> Unit) {
        //Todo Issue with get user-context token
    }

    companion object {
        const val BASE_URL = "https://api.twitter.com/"
        const val CONTENT_TYPE = "Content-Type"
        const val AUTHORIZATION = "Authorization"
        const val GEO = "1.1/geo/search.json"
        const val SEARCH = "1.1/search/tweets.json"
        const val QUERY = "?q="
        const val GEOCODE = "?geocode="
        const val OAUTH2 = "oauth2/token"
        const val GRANT_TYPE = "?grant_type="
        const val RESULT_TYPE = "result_type="
        const val CLIENT_CREDENTIALS = "client_credentials"
        const val RECENT = "recent"
        const val AND = "&"
        const val KM = "km"
        const val APPLICATION_JSON = "application/json"
        const val APPLICATION_XXX_FORM = "application/x-www-form-urlencoded;charset=UTF-8"
    }

}