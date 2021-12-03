package com.example.myapplication

class News (private var _imgSource: String,
            private var _link: String,
            private var _header: String,
            private var _date: String,
            private var _body: String){
    var imgSource: String
        set(value){
            _imgSource = value
        }
        get()=  _imgSource

    var link: String
        set(value){
            _link = value
        }
        get()=  _link

    var title: String
        set(value){
            _header = value
        }
        get()=  _header

    var date: String
        set(value){
            _date = value
        }
        get()=  _date

    var body: String
        set(value){
            _body = value
        }
        get()=  _body
}