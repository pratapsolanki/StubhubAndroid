package com.stubhub.event.model

data class EventManage(
    val events: MutableList<Event> = ArrayList()
)