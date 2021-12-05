package com.example.myapplication


import android.text.Editable

import android.text.Html
import android.util.Log

import org.xml.sax.XMLReader


class CustomTagHandler : Html.TagHandler {
    override fun handleTag(
        opening: Boolean, tag: String,
        output: Editable, xmlReader: XMLReader?
    ) {


        if (opening && tag == "customLi") {
            output.append("\n\u2022 ")
        }
    }

}