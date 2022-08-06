package com.stubhub.event.utils

import com.google.gson.Gson


object GsonUtil {
    private val gson = Gson()

    fun toJson(s: Any): String = gson.toJson(s)

    fun <T> fromJson(json: String, clazz: Class<T>): T = gson.fromJson<T>(json, clazz)
}

// json
fun Any.toJson() = GsonUtil.toJson(this)

inline fun <reified T> String.fromJson() = GsonUtil.fromJson(this, T::class.java)