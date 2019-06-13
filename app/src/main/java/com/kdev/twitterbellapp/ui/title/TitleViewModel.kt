package com.kdev.twitterbellapp.ui.title

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.kdev.twitterbellapp.data.model.local.SearchTweet
import com.kdev.twitterbellapp.data.model.local.TweetDetails
import com.kdev.twitterbellapp.data.network.Statuses
import com.kdev.twitterbellapp.ui.base.BaseViewModel
import com.kdev.twitterbellapp.utils.callback.Response
import com.kdev.twitterbellapp.utils.manager.PrefsManager
import kotlinx.coroutines.launch
import timber.log.Timber

class TitleViewModel(prefsManager: PrefsManager): BaseViewModel(prefsManager) {

    private var searchTweets = mutableListOf<SearchTweet>()

    fun fetchTweetsByLocation(lat: Double, long: Double){
        viewModelScope.launch {
            when(val result = dataRepository.fetchTweetsByLocation(lat, long)){
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

    /** Save Tweets Details for comparing them when will have a tweet location place */
    private fun saveTweetDetails(list: Statuses){
        val tweets = mutableListOf<TweetDetails>()
        list.statuses?.forEach { _tweet ->
            tweets.add(TweetDetails(_tweet.id.toString(), _tweet.text, _tweet.user?.id.toString()))
        }
    }

    fun fetchByQuery(query: String){
        viewModelScope.launch {
            when(val result = dataRepository.fetchTweetsByQuery(query)){
                is Response.Search.TweetReceived -> {
                    if(searchTweets.isNotEmpty()) searchTweets.clear()
                    result.data.statuses?.forEach {
                        searchTweets.add(SearchTweet(it.user?.profileImageUrl, it.user?.name, it.user?.screenName, it.text))
                    }
                    response.value = Response.Search.TweetByQueryReceived(searchTweets)
                }
                is Response.Search.TweetFailure -> response.value = result
            }
        }
    }

    fun generateBearer() {
        viewModelScope.launch {
            response.value = dataRepository.generateBearerToken()
        }
    }
}