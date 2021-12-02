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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), OnItemClickListener {

    var i = 21
    private val values = Array(10) {News(
        "",
        "Инновационный уикенд: «Техносреда - " + (i++).toString() + "»", "11-11-2021",
        "Санкт-Петербургский государственный электротехнический " +
                "университет «ЛЭТИ» принял участие в выставке и деловой " +
                "программе фестиваля науки и технических достижений " +
                "«Техносреда – 2021»"
    )}


    override fun onItemClicked(news: News, savedInstanceState: Bundle?) {
        val bundle = Bundle()
        bundle.putString("Header", news.header)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_back).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_HomeFragment)
        }
        // Get the text view
        val showHeaderTextView = view.findViewById<TextView>(R.id.textview_first)

        // Display the new value in the text view.
        showHeaderTextView.text = "Новости"

        val recyclerView = view.findViewById<RecyclerView>(R.id.news_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = NewsAdapter(values, this)
    }
}