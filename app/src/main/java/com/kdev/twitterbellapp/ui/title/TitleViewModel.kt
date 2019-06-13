package com.kdev.twitterbellapp.ui.title

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.kdev.twitterbellapp.data.model.local.TweetDetails
import com.kdev.twitterbellapp.data.network.Statuses
import com.kdev.twitterbellapp.ui.base.BaseViewModel
import com.kdev.twitterbellapp.utils.callback.Response
import com.kdev.twitterbellapp.utils.manager.PrefsManager
import kotlinx.coroutines.launch

class TitleViewModel(prefsManager: PrefsManager): BaseViewModel(prefsManager) {


    fun fetchTweetsByLocation(lat: Double, long: Double){
        viewModelScope.launch {
            val result = dataRepository.fetchTweetsByLocation(lat, long)
            when(result){
                is Response.Search.TweetReceived -> {
                    saveTweetDetails(result.data)
                    fetchTweetPlaces(lat, long)

                }
                is Response.Search.TweetFailure -> response.value = result
            }
        }
    }

    private suspend fun fetchTweetPlaces(lat: Double, long: Double){
        val result = dataRepository.fetchTweetsPlaces(lat, long)
    }

    private fun saveTweetDetails(list: Statuses){
        val tweets = mutableListOf<TweetDetails>()
        list.statuses?.forEach { _tweet ->
            tweets.add(TweetDetails(_tweet.id.toString(), _tweet.text, _tweet.user?.id.toString()))
        }
    }

    fun fetchByQuery(consumerKey: String, consumerSecret: String, query: String){

    }
}