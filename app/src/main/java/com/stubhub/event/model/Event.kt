package com.stubhub.event.model

data class Event(
    val artist: MutableList<String> = ArrayList(),
    val city: String,
    val date: String,
    val distanceFromVenue: Double,
    val id: Int,
    val name: String,
    val price: Int,
    val venueName: String
)