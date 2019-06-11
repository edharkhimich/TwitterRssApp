package com.kdev.twitterbellapp.utils.network

object Api {

    const val BASE_URL = "https://api.twitter.com/1.1/"
    const val GEO = "geo/search.json"
    const val QUERY = "?query="

    fun getTwittsByLocation(lat: Long, long: Long): String = BASE_URL + GEO + QUERY + "lat"

}