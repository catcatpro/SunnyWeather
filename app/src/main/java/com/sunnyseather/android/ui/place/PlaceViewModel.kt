package com.sunnyseather.android.ui.place

import androidx.lifecycle.*
import com.sunnyseather.android.logic.Repository
import com.sunnyseather.android.logic.model.Place

class PlaceViewModel:ViewModel(){
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String){
        searchLiveData.value = query
    }

}