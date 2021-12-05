package com.example.myapplication

class NewsElement (
    private var _elementType: Int,
    private var _elementHtml: String,
    private var _imgSources: List<String>?,
    private var _quoteBody: String,
    private var _quoteAuthor: String,
        ){
    var elementType: Int
        set(value){
            _elementType = value
        }
        get()=  _elementType

    var elementHtml: String
        set(value){
            _elementHtml = value
        }
        get()=  _elementHtml

    var imgSources: List<String>?
        set(value){
            _imgSources = value
        }
        get()=  _imgSources


    var quoteBody: String
        set(value){
            _quoteBody = value
        }
        get()=  _quoteBody

    var quoteAuthor: String
        set(value){
            _quoteAuthor = value
        }
        get()=  _quoteAuthor

}