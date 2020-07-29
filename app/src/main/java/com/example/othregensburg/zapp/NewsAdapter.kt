package com.example.othregensburg.zapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(private val newsFeed: NewsFeed) : RecyclerView.Adapter<NewsFeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.news_item, parent, false)

        return NewsFeedViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return newsFeed.news.count()
    }

    override fun onBindViewHolder(holder: NewsFeedViewHolder, position: Int) {

        val newsObj = newsFeed.news[position]

        holder.view.lbl_title?.text = newsObj.title
        holder.view.lbl_description?.text = newsObj.description

        holder.news = newsObj
    }

}

class NewsFeedViewHolder(val view: View, var news: News? = null) : RecyclerView.ViewHolder(view) {

//    init {
//        view.setOnClickListener {
//            Toast.makeText(view.context, "Click", Toast.LENGTH_SHORT).show()
//        }
//    }

}

class NewsFeed(val news: List<News>)

class News(val id: Int, val title: String, val description: String)

// JSON FILE FORMAT

//{
//    "id":0,
//    "title":"First News",
//    "description":"First News Description",
//},
