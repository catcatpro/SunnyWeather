package com.sunnyseather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyseather.android.MainActivity
import com.sunnyseather.android.R
import com.sunnyseather.android.databinding.FragmentPlaceBinding
import com.sunnyseather.android.logic.model.Place
import com.sunnyseather.android.logic.model.Weather
import com.sunnyseather.android.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter
    private lateinit var placeFragmentViewBinding:FragmentPlaceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        placeFragmentViewBinding = FragmentPlaceBinding.inflate(inflater, container, false)
        return placeFragmentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.isPlaceSaved()){
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        placeFragmentViewBinding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        placeFragmentViewBinding.recyclerView.adapter = adapter
        placeFragmentViewBinding.searchPlaceEdit.addTextChangedListener{ editable ->
            val content = editable.toString ()
            Log.d("resss",content)


            if (content.isNotEmpty())
                 viewModel.searchPlaces(content)
            else{
                placeFragmentViewBinding.recyclerView.visibility = View.GONE
                placeFragmentViewBinding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
            }
        }

//        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
//            val places = result.getOrNull()
//            if (places != null){
//                placeFragmentViewBinding.recyclerView.visibility  = View.VISIBLE
//                placeFragmentViewBinding.bgImageView.visibility = View.GONE
//                viewModel.placeList.clear()
//                viewModel.placeList.addAll(places as List<Place>)
//                adapter.notifyDataSetChanged()
//            }else{
//                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
//                result.exceptionOrNull()?.printStackTrace()
//            }
//        })

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null){
                placeFragmentViewBinding.recyclerView.visibility  = View.VISIBLE
                placeFragmentViewBinding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places as List<Place>)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

}