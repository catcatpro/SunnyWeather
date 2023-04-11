package com.sunnyseather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        //彩云天气token
        const val TOKEN = "iLc6g6ZhCFc85nQn"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}