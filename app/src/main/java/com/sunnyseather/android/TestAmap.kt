package com.sunnyseather.android


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import com.amap.api.services.poisearch.PoiSearchV2.OnPoiSearchListener
import com.sunnyseather.android.databinding.ActivityTestAampBinding


class TestAmap : AppCompatActivity(), OnGeocodeSearchListener , OnPoiSearchListener{
    lateinit var viewBinding:ActivityTestAampBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         viewBinding = ActivityTestAampBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        ServiceSettings.updatePrivacyAgree(this, true)
        ServiceSettings.updatePrivacyShow(this, true, true)
//        var geocoderSearch:GeocodeSearch

        viewBinding.queryBtn.setOnClickListener {
            try {
                val city = viewBinding.editAddress.text.toString()

//                geocoderSearch = GeocodeSearch(this)
//                geocoderSearch.setOnGeocodeSearchListener(this)
//                Log.d("city", city)
//                val query  = GeocodeQuery(city, "0766")
//                geocoderSearch.getFromLocationNameAsyn(query)
                val query = PoiSearchV2.Query(city, "","")
                query.setPageSize(10);// 设置每页最多返回多少条poiitem
                query.setPageNum(1);//设置查询页码
                val poiSearch = PoiSearchV2(this, query)
                poiSearch.setOnPoiSearchListener(this)
                poiSearch.searchPOIAsyn()
            }catch (e: AMapException){
//
            }



        }
    }
    override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
        p0?.geocodeQuery?.let { Log.d("SeachRes", it.locationName)
        }
    }


    override fun onPoiSearched(p0: PoiResultV2?, p1: Int) {
        p0?.pois?.let {
//            for (item in it){
//                Log.d("SearchRes.", item.adCode)
//            }
            viewBinding.coordinator.text = "城市编号：" + it[0].adCode
        }
//        p0?.pois?.let {
//            Log.d("SearchRes.", it[0].adCode)
//           viewBinding.coordinator.text = it[0].adCode
//        }
    }
    override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
        p0?.let {
            Log.d("SearchRes",it.adCode)
        }
    }
    companion object{
        @JvmStatic
        open  fun updatePrivacyShow(context: Context, isContains: Boolean, isShow: Boolean){
        }

        @JvmStatic
        open fun updatePrivacyAgree(context: Context?, isAgree: Boolean){

        }
    }
}