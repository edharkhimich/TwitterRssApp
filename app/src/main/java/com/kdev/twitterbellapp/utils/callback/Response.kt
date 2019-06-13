package com.kdev.twitterbellapp.utils.callback

import com.kdev.twitterbellapp.data.model.local.SearchTweet
import com.kdev.twitterbellapp.data.network.Statuses


sealed class Response<out T : Any> {

    sealed class Login {
        class TokenReceived(val data: Any? = null): Response<Any>()
        class TokenFailed(val data: Any? = null): Response<Any>()
    }

    sealed class Search {
        class TweetReceived(val data: Statuses): Response<Any>()
        class TweetFailure(val message: String? = null): Response<Any>()

        class TweetByQueryReceived(val data: List<SearchTweet>): Response<Any>()
    }

}