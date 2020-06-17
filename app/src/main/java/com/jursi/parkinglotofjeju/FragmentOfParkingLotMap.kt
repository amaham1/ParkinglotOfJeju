package com.jursi.parkinglotofjeju

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jursi.parkinglotofjeju.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_of_parking_lot_map.view.*
import net.daum.mf.map.api.*
import splitties.toast.toast


class FragmentOfParkingLotMap : Fragment() {

    //지도관련
    private var mapView: MapView? = null
    private var mCalloutBalloon: View? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private var aar1: String? = null
    private var aarr1: String? = null
    private var aar2: String? = null
    private var aarr2: String? = null
    private var aar3: String? = null
    private var aarr3: String? = null
    private var aar4: String? = null
    private var aarr4: String? = null
    private var aar5: String? = null
    private var aarr5: String? = null
    private var aar6: String? = null
    private var aarr6: String? = null
    private var aar7: String? = null
    private var aarr7: String? = null
    private var aar8: String? = null
    private var aarr8: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_of_parking_lot_map, container, false)
        //카카오 지도 불러오기
        mCalloutBalloon = layoutInflater.inflate(R.layout.custom_kakao_map_marker, null)
        mapView = MapView(activity)
        val mapViewContainer = rootView.findViewById(R.id.map_view) as RelativeLayout
        mapViewContainer.addView(mapView)
        mapView!!.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.500676, 126.529154), 9, true)//처음 보여질 중심점 변경

        //커스텀 마커
        val parkingViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        parkingViewModel.getStringISTL_LCTN_ADDR.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                getPlaceInfo(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7])
            }
        })

        //맵에 일반인용 표시
        parkingViewModel.getStringGNRL_RMND_PRZN_NUM
            .observe(viewLifecycleOwner, Observer { it ->
                it?.let {
                    aar1 = it[0]
                    aar2 = it[1]
                    aar3 = it[2]
                    aar4 = it[3]
                    aar5 = it[4]
                    aar6 = it[5]
                    aar7 = it[6]
                    aar8 = it[7]
                }
            })
        //맵에 장애인용 표시
        parkingViewModel.getStringHNDC_RMND_PRZN_NUM
            .observe(viewLifecycleOwner, Observer { it ->
                it?.let {
                    aarr1 = it[0]
                    aarr2 = it[1]
                    aarr3 = it[2]
                    aarr4 = it[3]
                    aarr5 = it[4]
                    aarr6 = it[5]
                    aarr7 = it[6]
                    aarr8 = it[7]

                }
            })
        mapView!!.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())

        //현위치표시
        val lm = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager?
        rootView.locatiofinder.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            } else {
                val location = lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음. GPS 원하면 GPS_PROVIDER <- 사용
                latitude = location?.latitude!!
                longitude = location.longitude
            }
            toast("현재위치를 불러옵니다.\n 시간이 걸릴 수 있어요")
            val marker = MapPOIItem()
            marker.itemName = "현재 위치"
            marker.tag = 15
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude)
            Log.d("1ee1", longitude.toString())
            marker.markerType = MapPOIItem.MarkerType.CustomImage // 마커 모양
            marker.customImageResourceId = R.drawable.pin
            marker.isShowCalloutBalloonOnTouch = false //마커 클릭불가
            mapView!!.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude)))
            mapView!!.addPOIItem(marker)
            /*mapView!!.currentLocationTrackingMode = //주기적으로 사용자 따라다니기
               MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            */
            rootView.locatiofinder.setAnimation("lottie_gps.json")
            rootView.locatiofinder.repeatCount = 1
            rootView.locatiofinder.playAnimation()
        }
        return rootView
    } //OnCreateView 끝
    //OnCreateView 끝
    //OnCreateView 끝


    //지도용 주차장 정보
    private fun getPlaceInfo(place1: String, place2: String, place3: String, place4: String, place5: String, place6: String, place7: String, place8: String) {
        val marker1 = MapPOIItem()
        val marker2 = MapPOIItem()
        val marker3 = MapPOIItem()
        val marker4 = MapPOIItem()
        val marker5 = MapPOIItem()
        val marker6 = MapPOIItem()
        val marker7 = MapPOIItem()
        val marker8 = MapPOIItem()

        //첫번째 주차장
        marker1.itemName = place1
        marker1.tag = 1
        marker1.mapPoint = MapPoint.mapPointWithGeoCoord(33.5165328, 126.5242137)
        marker1.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker1.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //두번째 주차장
        marker2.itemName = place2
        marker2.tag = 2
        marker2.mapPoint = MapPoint.mapPointWithGeoCoord(33.504836, 126.541985)
        marker2.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker2.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //세번째 주차장
        marker3.itemName = place3
        marker3.tag = 3
        marker3.mapPoint = MapPoint.mapPointWithGeoCoord(33.494744, 126.535357)
        marker3.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker3.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //네번째 주차장
        marker4.itemName = place4
        marker4.tag = 4
        marker4.mapPoint = MapPoint.mapPointWithGeoCoord(33.496849, 126.535061)
        marker4.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker4.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //다섯번째 주차장
        marker5.itemName = place5
        marker5.tag = 5
        marker5.mapPoint = MapPoint.mapPointWithGeoCoord(33.249773, 126.562327)
        marker5.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker5.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //여섯번째 주차장
        marker6.itemName = place6
        marker6.tag = 6
        marker6.mapPoint = MapPoint.mapPointWithGeoCoord(33.514693, 126.528236)
        marker6.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker6.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //일곱번째 주차장
        marker7.itemName = place7
        marker7.tag = 7
        marker7.mapPoint = MapPoint.mapPointWithGeoCoord(33.511346, 126.527429)
        marker7.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker7.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //여덟번째 주차장
        marker8.itemName = place8
        marker8.tag = 8
        marker8.mapPoint = MapPoint.mapPointWithGeoCoord(33.515990, 126.528923)
        marker8.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker8.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView!!.addPOIItem(marker1)
        mapView!!.addPOIItem(marker2)
        mapView!!.addPOIItem(marker3)
        mapView!!.addPOIItem(marker4)
        mapView!!.addPOIItem(marker5)
        mapView!!.addPOIItem(marker6)
        mapView!!.addPOIItem(marker7)
        mapView!!.addPOIItem(marker8)
    }
    //마크 클릭시 말풍선 정보
    inner class CustomCalloutBalloonAdapter : CalloutBalloonAdapter {
        override fun getCalloutBalloon(poiItem: MapPOIItem): View {
            //이미지 넣기용 (mCalloutBalloon?.findViewById<ImageView>(R.id.badge))?.setImageResource(R.drawable.ic_launcher_background)
            (mCalloutBalloon?.findViewById<TextView>(R.id.title))?.text = poiItem.itemName
            when (poiItem.tag) {
                1 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar1
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr1
                }
                2 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar2
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr2
                }
                3 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar3
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr3
                }
                4 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar4
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr4
                }
                5 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar5
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr5
                }
                6 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar6
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr6
                }
                7 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar7
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr7
                }
                8 -> {
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_GN))?.text = aar8
                    (mCalloutBalloon?.findViewById<TextView>(R.id.TV_HN))?.text = aarr8
                }
            }
            return mCalloutBalloon!!
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem): View? {
            return mCalloutBalloon!!
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView?.setShowCurrentLocationMarker(false)
    }

}