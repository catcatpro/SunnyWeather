package com.sunnyseather.android.ui.place

import androidx.lifecycle.*
import com.sunnyseather.android.logic.Repository
import com.sunnyseather.android.logic.model.Place

class PlaceViewModel:ViewModel(){
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()

    val placeLiveData = searchLiveData.switchMap(){query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String){
        searchLiveData.value = query
    }

    fun savePlace(place:Place) = Repository.savedPlace(place)

    fun getSavedPlace() = Repository.getSavePlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}