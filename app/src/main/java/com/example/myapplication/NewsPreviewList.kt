package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NewsPreviewList : Fragment() {

    var i = 21
    private val values = Array(10) {News(
        "",
        "",
        "Инновационный уикенд: «Техносреда - " + (i++).toString() + "»", "11-11-2021",
        "Санкт-Петербургский государственный электротехнический " +
                "университет «ЛЭТИ» принял участие в выставке и деловой " +
                "программе фестиваля науки и технических достижений " +
                "«Техносреда – 2021»"
    )}

    private val url = "https://etu.ru/ru/studentam/studencheskie-novosti/?start="
    private val listNews = mutableListOf<News>()
    private lateinit var adapter: NewsAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.news_preview_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_back).setOnClickListener {
            findNavController().navigate(R.id.action_NewsPreviewList_to_HomeFragment)
        }
        // Get the text view
        val showHeaderTextView = view.findViewById<TextView>(R.id.textview_first)

        // Display the new value in the text view.
        showHeaderTextView.text = "Новости"

        val recyclerView = view.findViewById<RecyclerView>(R.id.news_list)
        val linearLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = linearLayoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                GlobalScope.launch {
                    getData(page)
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener);

        adapter = NewsAdapter()

        recyclerView.adapter = adapter

        GlobalScope.launch {
            getData(0)
        }
    }

    private fun getData(page: Int){
        try {
            val document = Jsoup.connect(url + (page*10).toString())
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .get()
            val element = document.select("div.col-sm-6.preview")

            for (i in 0 until element.size){

                val newsTitle = element.select("h2.title")
                    .eq(i)
                    .select("a")
                    .text()

                val newsDate = element.select("p.date")
                    .eq(i)
                    .select("small.text-muted")
                    .text()

                val newsBody = element.select("p:not(.date)")
                    .eq(i)
                    .text()

                val imgSource = document.baseUri() + element.select("img")
                    .eq(i)
                    .attr("src")

                val newsLink = document.baseUri() + element.select("div.col-sm-6.preview>a")
                    .eq(i)
                    .attr("href")


                listNews.add(News(imgSource, newsLink, newsTitle, newsDate, newsBody))
            }
            GlobalScope.launch(Dispatchers.Main) {
                adapter.set(listNews)
            }

        }catch (ex: IOException){

        }
    }

}