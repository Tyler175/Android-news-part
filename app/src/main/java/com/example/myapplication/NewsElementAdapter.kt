package com.example.myapplication

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator




class TextViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val newsText: TextView = view.findViewById<TextView>(R.id.news_text)

    fun bind(newsElement: NewsElement)
    {
        newsText.text = HtmlCompat.fromHtml(
            newsElement.elementHtml,
            HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH,
            null,
            CustomTagHandler()
        )
        newsText.movementMethod = LinkMovementMethod.getInstance()
    }

}

class QuoteViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val quoteBody: TextView = view.findViewById<TextView>(R.id.quote_body)
    private val quoteAuthor: TextView = view.findViewById<TextView>(R.id.quote_author)
    fun bind(newsElement: NewsElement)
    {
        quoteBody.text = HtmlCompat.fromHtml(
            newsElement.quoteBody, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH, null,
            CustomTagHandler()
        )
        quoteBody.movementMethod = LinkMovementMethod.getInstance()

        quoteAuthor.text = HtmlCompat.fromHtml(
            newsElement.quoteAuthor, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
        )
        quoteAuthor.movementMethod = LinkMovementMethod.getInstance()
    }

}

class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val newsCarousel: ViewPager2 = view.findViewById<ViewPager2>(R.id.viewPager2)
    private val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

    fun bind(newsElement: NewsElement)
    {

        val data = newsElement.imgSources?.toMutableList()?: mutableListOf()
        data.add(0, newsElement.imgSources?.last().toString())
        data.add(newsElement.imgSources?.get(0).toString())

        with(newsCarousel) {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        if (currentItem == 0)
                            setCurrentItem(data.size - 2, false)
                        else if (currentItem == data.size - 1)
                            setCurrentItem(1, false)
                    }
                }
            })
            adapter = InfiniteViewPageAdapter(data)

            setCurrentItem(1, false)
        }
        TabLayoutMediator(tabLayout, newsCarousel) { tab, position ->


        }.attach()


        (tabLayout.getTabAt(0)!!.view as LinearLayout).visibility = View.GONE
        (tabLayout.getTabAt(data.size-1)!!.view as LinearLayout).visibility = View.GONE
    }

}

class NewsElementAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listNewsElements = mutableListOf<NewsElement>()



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            return TextViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(R.layout.news_text, viewGroup, false)
            )
        } else if (viewType == 2){
            return QuoteViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.news_quote,
                    viewGroup,
                    false
                )
            )
        } else {
            return CarouselViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.infinite_carousel,
                    viewGroup,
                    false
                )
            )
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (listNewsElements[position].elementType == 1) {
            (viewHolder as TextViewHolder).bind(listNewsElements[position])
        } else if ((listNewsElements[position].elementType == 2)) {
            (viewHolder as QuoteViewHolder).bind(listNewsElements[position])
        } else {

            (viewHolder as CarouselViewHolder).bind(listNewsElements[position])
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listNewsElements.size

    override fun getItemViewType(position: Int): Int {
        return listNewsElements[position].elementType
    }

    fun set(list: MutableList<NewsElement>){
        this.listNewsElements.clear()
        this.listNewsElements.addAll(list)
        notifyDataSetChanged()
    }
}