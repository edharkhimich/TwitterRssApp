package com.kdev.twitterbellapp.data.repository

import android.location.Location
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.kdev.twitterbellapp.TwitterBellApp
import com.kdev.twitterbellapp.data.network.Statuses
import com.kdev.twitterbellapp.utils.Constants.AUTH_TOKEN_KEY
import com.kdev.twitterbellapp.utils.Constants.BEARER_TOKEN_KEY
import com.kdev.twitterbellapp.utils.Constants.DEF
import com.kdev.twitterbellapp.utils.Constants.SECRET_KEY
import com.kdev.twitterbellapp.utils.callback.Response
import com.kdev.twitterbellapp.utils.common.GsonUtils
import com.kdev.twitterbellapp.utils.common.SingletonHolder
import com.kdev.twitterbellapp.utils.manager.PrefsManager
import com.kdev.twitterbellapp.utils.network.APIController
import com.kdev.twitterbellapp.utils.network.ServiceVolley
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.APPLICATION_XXX_FORM
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.AUTHORIZATION
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.BASE_URL
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.CLIENT_CREDENTIALS
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.CONTENT_TYPE
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.GRANT_TYPE
import com.kdev.twitterbellapp.utils.network.ServiceVolley.Companion.OAUTH2
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DataRepositoryImpl constructor(private val prefs: PrefsManager) : DataRepository {

    private val lastKnownLocation = MutableLiveData<Location>()

    private val service by lazy { ServiceVolley() }
    private val apiController by lazy { APIController(service) }

    override fun getLastKnownDeviceLocation(): Location? = lastKnownLocation.value

    override fun getObservableLastKnownDeviceLocation(): MutableLiveData<Location> = lastKnownLocation

    override fun setLastKnownDeviceLocation(location: Location?) {
        location?.let { lastKnownLocation.value = location }
    }

    override suspend fun fetchTweetsByLocation(lat: Double, long: Double): Response<Any>? {
        var result: Response<Any>? = null
        if (getBearerToken().isNullOrEmpty()) {
            val authHeader = getAuthorizationHeader(prefs.consumerKey, prefs.consumerSecret)
            getBearerToken(authHeader) { response ->
                GlobalScope.launch {
                    result = if (response != null) {
                        val tkn = response.getString(ACCESS_TOKEN)
                        prefs.saveString(BEARER_TOKEN_KEY, tkn)
                        getTweetsByLocation(tkn, lat, long)
                    } else Response.Search.TweetFailure()
                }
            }
        } else result = getTweetsByLocation(getBearerToken()!!, lat, long)

        return result
    }

    private suspend fun getTweetsByLocation(token: String, lat: Double, long: Double): Response<Any>? {
        val result: Response<Any>?
        result = suspendCoroutine<Response<Any>> {
            GlobalScope.launch {
                apiController.getTweetsByLocation(token, lat, long, { _response ->
                    if (_response != null) {
                        val gson = GsonUtils.getInstance().gson
                        if (gson != null) {
                            val jsonObject = JsonParser().parse(_response.toString()) as JsonObject
                            val type = object : TypeToken<Statuses>() {}.type
                            val tweetStatuses = gson.fromJson<Statuses>(jsonObject, type)
                            it.resume(Response.Search.TweetReceived(tweetStatuses))
                        } else it.resume(Response.Search.TweetFailure())
                    }
                })
            }
        }
        return result
    }

    override suspend fun fetchTweetsPlaces(lat: Double, long: Double): Response<Any>? {
        apiController.getTweetsPlaces(
            getAuthorizationHeader(prefs.consumerKey, prefs.consumerSecret),
            lat,
            long,
            completionHandler = { response ->
                // TODO Don't have user-context auth for it
            })
        return Response.Search.TweetFailure()
    }

    fun getBearerToken(basicAuthHeader: String?, completionHandler: (response: JSONObject?) -> Unit) {
        val request = object : JsonObjectRequest(
            Method.POST,
            BASE_URL + OAUTH2 + GRANT_TYPE + CLIENT_CREDENTIALS,
            null,
            com.android.volley.Response.Listener<JSONObject> { response -> completionHandler(response) },
            com.android.volley.Response.ErrorListener { error ->
            }
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

    private fun getAuthorizationHeader(consumerKey: String, consumerSecret: String): String? {
        return try {
            val consumerKeyAndSecret = "$consumerKey:$consumerSecret"
            val data = consumerKeyAndSecret.toByteArray(charset(UTF_8))
            val base64 = Base64.encodeToString(data, Base64.NO_WRAP)
            BASIC + base64
        } catch (e: UnsupportedEncodingException) {
            null
        }
    }

    override suspend fun fetchTweetsByQuery(query: String): Response<Any>? {
        var result: Response<Any>? = null
        if (getBearerToken().isNullOrEmpty()) {
            val authHeader = getAuthorizationHeader(prefs.consumerKey, prefs.consumerSecret)
            getBearerToken(authHeader) { response ->
                GlobalScope.launch {
                    result = if (response != null) {
                        val tkn = response.getString(ACCESS_TOKEN)
                        prefs.saveString(BEARER_TOKEN_KEY, tkn)
                        getTweetsByQuery(tkn, query)
                    } else Response.Search.TweetFailure()
                }
            }
        } else result = getTweetsByQuery(getBearerToken()!!, query)
        return result
    }

    private suspend fun getTweetsByQuery(token: String, query: String): Response<Any>? {
        val result: Response<Any>?
        result = suspendCoroutine<Response<Any>> {
            GlobalScope.launch {
                apiController.getTweetsByQuery(token, query) { _response ->
                    if (_response != null) {
                        val gson = GsonUtils.getInstance().gson
                        if (gson != null) {
                            val jsonObject = JsonParser().parse(_response.toString()) as JsonObject
                            val type = object : TypeToken<Statuses>() {}.type
                            val tweetStatuses = gson.fromJson<Statuses>(jsonObject, type)
                            it.resume(Response.Search.TweetReceived(tweetStatuses))
                        } else it.resume(Response.Search.TweetFailure())
                    }
                }
            }
        }
        return result
    }

    override fun saveAuthData(token: String, secret: String) {
        prefs.run {
            saveString(AUTH_TOKEN_KEY, token)
            saveString(SECRET_KEY, secret)
        }
    }

    override fun getAuthToken(): String? = prefs.receiveString(AUTH_TOKEN_KEY, DEF)

    override fun getBearerToken(): String? = prefs.receiveString(BEARER_TOKEN_KEY, DEF)

    companion object : SingletonHolder<DataRepositoryImpl, PrefsManager>(::DataRepositoryImpl) {
        const val BEARER = "Bearer "
        const val ACCESS_TOKEN = "access_token"
        const val UTF_8 = "UTF-8"
        const val BASIC = "Basic "
    }
}