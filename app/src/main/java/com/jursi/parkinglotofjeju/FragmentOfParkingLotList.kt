package com.jursi.parkinglotofjeju

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_of_parking_lot_list.*
import kotlinx.android.synthetic.main.fragment_of_parking_lot_list.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import splitties.toast.toast
import java.io.InputStreamReader
import java.net.URL


/*var parkinglotInfoArrayList: ArrayList<ParkinglotInfo>? = null // ParkinglotInfo 데이터 클래스 리스트 참조용 변수
var classOfParkinglotInfo: ParkinglotInfo? = null  //ParkinglotInfo 데이터 클래스 참조용 변수
var requestUrl: String? = null //API주소 참조용 변수
var aTpm: FragmentOfParkingLotList.ParkinglotParser? = null //ParkinglotParser Asycn 참조용 변수
var parserAdapter: recyclerViewAdapter? = null //RecyclerViewAdapter 클래스 참조용 변수*/


class FragmentOfParkingLotList : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_of_parking_lot_list, container, false)
        return rootView
    }



}
