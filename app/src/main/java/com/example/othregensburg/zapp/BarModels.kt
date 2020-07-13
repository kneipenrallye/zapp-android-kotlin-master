package com.example.othregensburg.zapp

class HomeFeed(val bars: List<Bars>)

class Bars(val id: Int, val name: String, val description: String, val img_preview: String, val img_bar: String, val location: String, val special: String)

// JSON FILE FORMAT

//{
//    "id":0,
//    "name":"Bar 13",
//    "description":"Cocktailbar in Regensburg",
//    "img_preview":"https://lh4.googleusercontent.com/-9JhIiQKEaTA/AAAAAAAAAAI/AAAAAAAAAAA/kLNYyr5JybU/s55-p-k-no-ns-nd/photo.jpg"
//    "img_bar":"https://lh5.googleusercontent.com/p/AF1QipN52uwCrR_0zpYMn9uM3FctEVPXn-voIiOaTF2A=w408-h301-k-no",
//    "location":"Keplerstraße 13, 93047 Regensburg",
//    "special":"3x Shots 2\n1x Cocktail 4"
//},
