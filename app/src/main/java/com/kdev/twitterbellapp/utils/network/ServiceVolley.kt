package com.kdev.twitterbellapp.utils.network

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.kdev.twitterbellapp.TwitterBellApp
import com.kdev.twitterbellapp.data.repository.DataRepositoryImpl.Companion.BEARER
import org.json.JSONObject

class ServiceVolley : ServiceInterface {

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
    }

    val TAG = ServiceVolley::class.java.simpleName

    override suspend fun getOAuthToken(basicAuthHeader: String?, completionHandler: (response: JSONObject?) -> Unit) {
        val request = object : JsonObjectRequest(
            Method.POST,
            BASE_URL + OAUTH2 + GRANT_TYPE + "client_credentials",
            null,
            Response.Listener<JSONObject> { response -> completionHandler(response) },
            Response.ErrorListener { error ->
                error
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers[CONTENT_TYPE] = "application/x-www-form-urlencoded;charset=UTF-8"
                headers[AUTHORIZATION] = basicAuthHeader ?: ""
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
        val url = BASE_URL + SEARCH + GEOCODE + lat + "," + long + "," + radius + "km"
        val jsonObjReq = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                completionHandler.invoke(response)
            },
            Response.ErrorListener { error ->
                error
                completionHandler.invoke(null)
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers[CONTENT_TYPE] = "application/json"
                headers[AUTHORIZATION] = BEARER + accessToken
                return headers
            }
        }

        TwitterBellApp.instance?.addToRequestQueue(jsonObjReq)
    }
}