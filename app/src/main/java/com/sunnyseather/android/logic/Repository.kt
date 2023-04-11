package com.sunnyseather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyseather.android.logic.model.Place
import com.sunnyseather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){

        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e: Exception){
            Log.d("resssa",e.message.toString())
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }


}