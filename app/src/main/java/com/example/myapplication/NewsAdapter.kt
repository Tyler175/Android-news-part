package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val newsTitle: TextView = view.findViewById<TextView>(R.id.news_title)
    private val newsBody: TextView = view.findViewById<TextView>(R.id.news_description)
    private val newsDate: TextView = view.findViewById<TextView>(R.id.news_date)
    private val newsImage: ImageView = view.findViewById<ImageView>(R.id.news_image)
    private var link = ""

    init {
        view.setOnClickListener{
            view.findNavController().navigate(NewsPreviewListDirections.actionNewsPreviewListToFullNews(link))
        }
    }

    fun bind(news: News)
    {
        newsTitle.text = news.title
        newsBody.text = news.body
        newsDate.text = news.date
        if (news.imgSource != "") Picasso.get().load(news.imgSource).placeholder(R.drawable.news_image).error(R.drawable.news_image).into(newsImage)

        link = news.link
    }

}

class NewsAdapter() :
    RecyclerView.Adapter<NewsViewHolder>() {

    private val listNews = mutableListOf<News>()



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NewsViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.news_preview, viewGroup, false)

        return NewsViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(newsViewHolder: NewsViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val news = listNews[position]
        newsViewHolder.bind(news)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listNews.size

    fun set(list: MutableList<News>){
        this.listNews.clear()
        this.listNews.addAll(list)
        notifyDataSetChanged()
    }
}