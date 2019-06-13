package com.kdev.twitterbellapp.ui.title

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kdev.twitterbellapp.R
import com.kdev.twitterbellapp.data.model.local.SearchTweet
import com.kdev.twitterbellapp.utils.view.getSpannableText
import kotlinx.android.synthetic.main.item_search.view.*

class TitleAdapter: RecyclerView.Adapter<TitleAdapter.TitleVH>() {

    private val items = mutableListOf<SearchTweet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return TitleVH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TitleVH, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<SearchTweet>){
        if(this.items.isNotEmpty()) this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItems(){
        this.items.clear()
        notifyDataSetChanged()
    }

    class TitleVH(private val v: View): RecyclerView.ViewHolder(v){

        fun bind(item: SearchTweet){
            Glide.with(v.context)
                .asDrawable()
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(120)))
                .load(item.image)
                .into(v.user_image)

            v.user_name.text = item.name
            v.user_login.text = item.login

            v.user_tweet_description.text = v.context.getSpannableText(item.description ?: "")
        }
    }
}