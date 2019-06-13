package com.kdev.twitterbellapp.utils.network

import org.json.JSONObject

class APIController constructor(serviceInjection: ServiceInterface) : ServiceInterface {

    override suspend fun getTweetsPlaces(
        basicAuthHeader: String?,
        lat: Double,
        long: Double,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        service.getTweetsPlaces(basicAuthHeader, lat, long, completionHandler)
    }

    override suspend fun getOAuthToken(basicAuthHeader: String?, completionHandler: (response: JSONObject?) -> Unit) {
        service.getOAuthToken(basicAuthHeader, completionHandler)
    }

    override suspend fun getTweetsByLocation(accessToken: String?, lat: Double, long: Double, completionHandler: (response: JSONObject?) -> Unit, radius: Int) {
        service.getTweetsByLocation(accessToken, lat, long, completionHandler, radius)
    }

    private val service: ServiceInterface = serviceInjection

}