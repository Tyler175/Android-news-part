package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.full_news.*
import kotlinx.android.synthetic.main.news_preview.news_date
import kotlinx.android.synthetic.main.news_preview.news_description
import kotlinx.android.synthetic.main.news_preview.news_image
import kotlinx.android.synthetic.main.news_preview.news_title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FullNews: Fragment(), CoroutineScope{

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val listNewsElement = mutableListOf<NewsElement>()
    private lateinit var adapter: NewsElementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.full_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //relative_layout.setPadding(0, 0, 0, constraintLayout.height)

        constraintLayout.addOnLayoutChangeListener { v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas ->

            val heightWas = bottomWas - topWas // Bottom exclusive, top inclusive
            if (v.height != heightWas) {
                scroll_view.setPadding(0, 0, 0, v.height)
            }
        }

        view.findViewById<Button>(R.id.button_back).setOnClickListener {
            findNavController().navigate(R.id.action_FullNews_to_NewsPreviewList)
        }


        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        adapter = NewsElementAdapter()
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.adapter = adapter

        job = launch(Dispatchers.IO) {
            getData()

        }
    }

    private fun getData(){
        try {
            val document = Jsoup.connect(arguments?.getString("link") ?: "")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .get()

            val title = document.select("h1.page-header").text()

            val imageSource = document.baseUri() + document.select("div#content img").attr("src")

            val description = document.select("p.lead").text()

            val date = document.select("span.pub-date").text()

            val views = document.select("span.hits").text()

            val content = document.select("div#content>p, div#content>blockquote, div#content>ul, div#content>h2, div#content>h3, div#content>div.carousel.slide")

            for (element in content) {
                if (element.normalName()=="blockquote"){
                    val body = element.select("p").html()
                    val author = element.select("footer").html()
                    listNewsElement.add(NewsElement(2, "", null, body, author))
                } else if(element.normalName()=="div"){
                    val images = element.select("img")
                    val imageSources = mutableListOf<String>()
                    for (image in images){
                        imageSources.add(document.baseUri() + image.attr("src"))
                    }
                    listNewsElement.add(NewsElement(3, "", imageSources, "", ""))
                }
                else if (element.html()!="") {
                    val text = element.html()+"<br>"
                    listNewsElement.add(NewsElement(1, text, null, "", ""))
                }


            }

            job = launch {
                news_title.text = title
                news_description.text = description
                news_date.text = date
                news_views.text = views



                if (imageSource != "") Picasso.get().load(imageSource).placeholder(R.drawable.news_image).error(
                    R.drawable.news_image
                ).into(news_image)
                adapter.set(listNewsElement)


            }

        } catch (ex: IOException){
            Log.e("test", ex.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}