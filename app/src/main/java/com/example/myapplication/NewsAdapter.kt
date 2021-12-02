package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val newsHeader: TextView = view.findViewById<TextView>(R.id.news_header)
    val newsBody: TextView = view.findViewById<TextView>(R.id.news_body)
    val newsDate: TextView = view.findViewById<TextView>(R.id.news_date)
    fun bind(news: News, clickListener: OnItemClickListener)
    {
        newsHeader.text = news.header
        newsBody.text = news.body
        newsDate.text = news.date
        itemView.setOnClickListener {
            clickListener.onItemClicked(news, Bundle())
        }
    }

}

class NewsAdapter(private val dataSet: Array<News>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<NewsViewHolder>() {

    

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
        val news = dataSet.get(position)
        newsViewHolder.bind(news, itemClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
interface OnItemClickListener{
    fun onItemClicked(news: News, savedInstanceState: Bundle?)
}