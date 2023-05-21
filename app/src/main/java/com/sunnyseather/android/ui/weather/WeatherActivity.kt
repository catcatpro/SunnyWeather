package com.sunnyseather.android.ui.weather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyseather.android.R
import com.sunnyseather.android.databinding.ActivityWeatherBinding
import com.sunnyseather.android.logic.model.Weather
import com.sunnyseather.android.logic.model.getSky
import com.sunnyseather.android.ui.place.PlaceFragment
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    lateinit var viewBinding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)
        viewBinding = ActivityWeatherBinding.inflate(layoutInflater)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        setContentView(viewBinding.root)

        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra("location_lng")?:""
        }

        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lat")?:""
        }

        if (viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name")?:""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            viewBinding.swipeRefresh.isRefreshing = false
        })
//        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)


        viewBinding.swipeRefresh.setColorSchemeResources(R.color.purple_200)
        refreshWeather()
        viewBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        viewBinding.now.navBtn.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        viewBinding.now.navBtn.setOnClickListener{
            viewBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        viewBinding.drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }


            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })

    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        viewBinding.swipeRefresh.isRefreshing = true
    }
    private fun showWeatherInfo(weater:Weather){
       viewBinding.now.placeName.text = viewModel.placeName
        val realtime = weater.realtime
        val daily =  weater.daily
        //填充now.xml布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} °C"
        viewBinding.now.currentTemp.text = currentTempText
        viewBinding.now.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        viewBinding.now.currentAQI.text = currentPM25Text
        viewBinding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
         //填充forecast.xml布局中的文件
        viewBinding.forecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until  days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forcast_item,viewBinding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText  = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"
            temperatureInfo.text = tempText
            viewBinding.forecast.forecastLayout.addView(view)
        }

        //填充life_index.xml 布局中的数据
        val lifeIndex = daily.lifeIndex
        val lifeIndexLayout =  viewBinding.lifeIndex
        lifeIndexLayout.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexLayout.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexLayout.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexLayout.carWashingText.text = lifeIndex.carWashing[0].desc
        viewBinding.weatherLayout.visibility = View.VISIBLE
    }



}