package com.kdev.twitterbellapp.utils.network

import org.json.JSONObject
import kotlin.coroutines.suspendCoroutine

interface ServiceInterface {

//    fun getTwittsByLocation(lat: Long, long: Long): String = BASE_URL + GEO + QUERY + "lat"

    suspend fun getOAuthToken(basicAuthHeader: String?, completionHandler: (response: JSONObject?) -> Unit)

    suspend fun getTweetsByLocation(accessToken: String?, lat: Double, long: Double, completionHandler: (response: JSONObject?) -> Unit,  radius: Int = 5)

//    fun getTweetsByLocation(path: String, params: )


}