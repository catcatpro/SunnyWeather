package com.sunnyseather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.ViewModel
import com.sunnyseather.android.logic.Repository
import com.sunnyseather.android.logic.model.Location

class WeatherViewModel :ViewModel(){
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName =""

    val weatherLiveData = locationLiveData.switchMap(){location->
        Repository.refreshWeather(locationLng, locationLat)
    }

    fun refreshWeather(lng:String, lat: String){
        locationLiveData.value = Location(lng, lat)
    }
}