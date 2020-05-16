package com.jursi.parkinglotofjeju

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_of_parking_lot_list.*
import kotlinx.android.synthetic.main.parkinglotitem.*
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeStamp.getTime
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import splitties.toast.toast
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityOfParkingLotList : AppCompatActivity() {
    //주차장 데이터 전용
    var parkinglotAsync: ParkinglotParser? = null //ParkinglotParser Asycn 참조용 변수
    var getUPDT_DTTime: String? = null
    //카운트용
    var cntValue = 30
    var cntTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_of_parking_lot_list)
    //리사이클러뷰 지정
        recyclerViewOfMain?.setHasFixedSize(true)
        val lm = LinearLayoutManager(applicationContext)
        lm.orientation = LinearLayoutManager.VERTICAL
        recyclerViewOfMain?.layoutManager = lm
    //새로고침 버튼
        refresh.setOnClickListener {
                if (cntValue == 0) {
                    //aTpm = ParkinglotParser()
                    //aTpm?.execute(100)
                    toast("완료")
                    cntValue = 30
                    cntTimer?.start()
                } else toast("$cntValue 초 기다려요")
        }
    //카운트용
        cntTimer = object : CountDownTimer(30010, 1000) {
            override fun onFinish() {
                cntValue = 0
            }

            override fun onTick(millisUntilFinished: Long) {
                cntValue--
            }
        }
    } //onCreate
    //서버시간용
    val TIME_SERVER = "time.google.com"
    var getPresentTime: String? = null //현재시간
    var decreasedTime: Int? = null
    var calculatedTiem: String? = null
    //parkinglotAsync 처음시작 부분
    //cntTimer? 처음시작 부분
    override fun onStart() {
        super.onStart()
        shimmer_view_container.startShimmerAnimation()
        parkinglotAsync = ParkinglotParser()
        parkinglotAsync?.execute(100)
        cntTimer?.start()
    }
    //주차장 데이터 파싱
    var parkinglotInfoArrayList: ArrayList<ParkinglotInfoItem>? = null // ParkinglotInfo 데이터 클래스 리스트 참조용 변수
    var ItemOfParkinglotInfo: ParkinglotInfoItem? = null  //ParkinglotInfo 데이터 클래스 참조용 변수
    var requestUrl: String? = null //API주소 참조용 변수
    var parserAdapter: recyclerViewAdapter? = null //RecyclerViewAdapter 클래스 참조용 변수
    inner class ParkinglotParser : AsyncTask<Int?, Int?, Int?>() {
        var urlCnt = 1 //URL 주소 증가용
        override fun doInBackground(vararg params: Int?): Int? {
            parkinglotInfoArrayList = ArrayList()
            while (urlCnt <= 2) {
                //요청할 url를 만듦
                requestUrl =
                    "http://openapi.jejuits.go.kr/OPEN_API/pisInfo/getPisInfo?serviceKey=7eF3P0tgu3a8X%2BQzp%2Bj6hmRurphNav%2Bm8orOWMH4zpGUdGD69gHoPQVZfDnH974%2FQQ9h0Ym7feZns1ffaEg8bQ%3D%3D&pageNo=$urlCnt&startPage=1&numOfRows=1&pageSize=10"
                //try를 통해 요청을 시작
                try {
                    Thread.sleep(1000)
                    var itemList = false //가져올 XML라인의 첫 태크
                    var ISTL_LCTN_ADDR = false //주차장 이름
                    var UPDT_DT = false //주차장 정보 갱신 시간
                    var GNRL_RMND_PRZN_NUM = false // 일반 주차 잔여개수
                    var HNDC_RMND_PRZN_NUM = false // 장애인 주차 잔여개수
                    var LGVH_RMND_PRZN_NUM = false // 경차 주차 잔여개수
                    var EMVH_RMND_PRZN_NUM = false // 긴급차량 주차 잔여개수
                    var HVVH_RMND_PRZN_NUM = false // 대형 잔여 주차구역 개수

                    val url = URL(requestUrl)
                    val `is` = url.openStream() //URL을 열어줌
                    val factory = XmlPullParserFactory.newInstance()
                    val parser = factory.newPullParser()
                    parser.setInput(InputStreamReader(`is`, "UTF-8"))

                    var eventType = parser.eventType
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> {
                            }
                            XmlPullParser.START_TAG -> {
                                when (parser.name) {
                                    "itemList" -> {
                                        ItemOfParkinglotInfo = ParkinglotInfoItem()
                                    }
                                    "ISTL_LCTN_ADDR" -> ISTL_LCTN_ADDR = true
                                    "UPDT_DT" -> UPDT_DT = true
                                    "GNRL_RMND_PRZN_NUM" -> GNRL_RMND_PRZN_NUM = true
                                    "HNDC_RMND_PRZN_NUM" -> HNDC_RMND_PRZN_NUM = true
                                    "LGVH_RMND_PRZN_NUM" -> LGVH_RMND_PRZN_NUM = true
                                    "EMVH_RMND_PRZN_NUM" -> EMVH_RMND_PRZN_NUM = true
                                    "HVVH_RMND_PRZN_NUM" -> HVVH_RMND_PRZN_NUM = true
                                }
                            }
                            //파서한 데이터를 각 변수에 담는다
                            XmlPullParser.TEXT -> {
                                when {
                                    ISTL_LCTN_ADDR -> {
                                        ItemOfParkinglotInfo?.saveISTL_LCTN_ADDR(parser.text)

                                        ISTL_LCTN_ADDR = false
                                    }
                                    UPDT_DT -> {
                                        ItemOfParkinglotInfo?.saveUPDT_DT(parser.text)
                                        getUPDT_DTTime = parser.text
                                        ItemOfParkinglotInfo?.saveTV_updatedTime(net())
                                        UPDT_DT = false
                                    }
                                    GNRL_RMND_PRZN_NUM -> {
                                        ItemOfParkinglotInfo?.saveGNRL_RMND_PRZN_NUM(parser.text)
                                        GNRL_RMND_PRZN_NUM = false
                                    }
                                    HNDC_RMND_PRZN_NUM -> {
                                        ItemOfParkinglotInfo?.saveHNDC_RMND_PRZN_NUM(parser.text)
                                        HNDC_RMND_PRZN_NUM = false
                                    }
                                    LGVH_RMND_PRZN_NUM -> {
                                        ItemOfParkinglotInfo?.saveLGVH_RMND_PRZN_NUM(parser.text)
                                        LGVH_RMND_PRZN_NUM = false
                                    }
                                    EMVH_RMND_PRZN_NUM -> {
                                        ItemOfParkinglotInfo?.saveEMVH_RMND_PRZN_NUM(parser.text)
                                        EMVH_RMND_PRZN_NUM = false
                                    }
                                    HVVH_RMND_PRZN_NUM -> {
                                        ItemOfParkinglotInfo?.saveHVVH_RMND_PRZN_NUM(parser.text)
                                        HVVH_RMND_PRZN_NUM = false
                                    }
                                }

                            }
                            XmlPullParser.END_TAG -> {
                                when {
                                    parser.name == "itemList" && ItemOfParkinglotInfo != null -> parkinglotInfoArrayList!!.add(
                                        ItemOfParkinglotInfo!!
                                    )
                                }
                            }
                            XmlPullParser.END_DOCUMENT -> {

                            }
                        }
                        eventType = parser.next()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                urlCnt++
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(values[0])
            //pD.progress = values[0]!!
        }

        override fun onCancelled() {
            super.onCancelled()
            //pD.dismiss()
        }

        override fun onPostExecute(s: Int?) {
            super.onPostExecute(s)
            shimmer_view_container.stopShimmerAnimation()
            shimmer_view_container.visibility = GONE
            toast("주차장 정보를 불러왔어요")
            parserAdapter = recyclerViewAdapter(applicationContext, parkinglotInfoArrayList!!)
            recyclerViewOfMain?.adapter = parserAdapter
        }
    }
    //서버시간 받아서 시간계산 후 ParkinglotParser로 넘겨줌
    fun net(): String? {
        try {
            val timeClient = NTPUDPClient()
            val inetAddress = InetAddress.getByName(TIME_SERVER)
            val timeInfo = timeClient.getTime(inetAddress)
            val returnTime = timeInfo.message.transmitTimeStamp.time
            val date = Date(returnTime)
            getPresentTime = SimpleDateFormat("yyyyMMddkkmmss").format(date)
            val dataFormat = SimpleDateFormat("yyyyMMddkkmmss", Locale.KOREA)
            val startDate = dataFormat.parse(getPresentTime)
            val endDate = dataFormat.parse("$getUPDT_DTTime")
            decreasedTime = ((startDate.time - endDate.time) / 1000).toInt()
            calculatedTiem = when {
                decreasedTime!! < 60 -> "${decreasedTime!! % 60}초 전 주차장 상황"
                decreasedTime!! in 60..3599 -> "${decreasedTime!! % 3600 / 60}분 ${decreasedTime!! % 60}초 전 주차장 상황"
                decreasedTime!! in 3600..86399 -> "${decreasedTime!! / 3600}시간 ${decreasedTime!! % 3600 / 60}분 전 주차장 상황"
                decreasedTime!! >= 86400 -> "${decreasedTime!! / 86400}일 전 주차장 상황"
                else -> "시간을 계산할 수 없어요"
            }
        } catch (e: Exception) {
        }
        return calculatedTiem
    }

}
