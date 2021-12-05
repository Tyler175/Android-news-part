package com.example.myapplication

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class CarouselAdapter(private val imageSources: List<String>?, private val context: Context): PagerAdapter() {


    override fun getCount(): Int {
        return imageSources?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView: ImageView = ImageView(context)
        Picasso.get().load(imageSources?.get(position)).placeholder(R.drawable.news_image).error(R.drawable.news_image).into(imageView)
        container.addView(imageView)
        return imageView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
