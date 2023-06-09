package com.sunnyseather.android.logic.network

import com.sunnyseather.android.SunnyWeatherApplication
import com.sunnyseather.android.logic.model.DailyResponse
import com.sunnyseather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime")
    fun getRealtimeWeather(@Path("lng") lng:String, @Path("lat") lat:String):Call<RealtimeResponse>

    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily?dailysteps=7")
    fun getDailyWeather(@Path("lng") lng:String, @Path("lat") lat:String):Call<DailyResponse>
}