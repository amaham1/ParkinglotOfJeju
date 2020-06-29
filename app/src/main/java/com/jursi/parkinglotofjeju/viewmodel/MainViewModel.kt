package com.jursi.parkinglotofjeju.viewmodel

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jursi.parkinglotofjeju.data.ParkinglotInfoItem
import org.apache.commons.net.ntp.NTPUDPClient
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import splitties.toast.toast
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {
    //서버시간 받아서 시간계산 후 넘겨줌
    fun getNetTime(): String? {
        try {
            val timeClient = NTPUDPClient()
            val inetAddress = InetAddress.getByName(TIME_SERVER)
            val timeInfo = timeClient.getTime(inetAddress)
            val returnTime = timeInfo.message.transmitTimeStamp.time
            val date = Date(returnTime)
            getPresentTime = SimpleDateFormat("yyyyMMddkkmmss").format(date)
            val dataFormat = SimpleDateFormat("yyyyMMddkkmmss", Locale.KOREA)
            val startDate = dataFormat.parse(getPresentTime)
            val endDate = dataFormat.parse(getUPDT_DTTime)
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

    //파싱데이터 뷰모델로 전달용
    var stringISTL_LCTN_ADDR = ArrayList<String>()
    var stringGNRL_RMND_PRZN_NUM = ArrayList<String>()
    var stringHNDC_RMND_PRZN_NUM = ArrayList<String>()

    //서버시간용
    val TIME_SERVER = "time.google.com"
    private var getPresentTime: String? = null //현재시간
    var decreasedTime: Int? = null
    var calculatedTiem: String? = null

    //파싱용
    private var requestUrl: String? = null //API주소 참조용 변수
    private var itemOfParkinglotInfo: ParkinglotInfoItem? = null  //ParkinglotInfo 데이터 클래스 참조용 변수
    private var getUPDT_DTTime: String? = null  //서버 시간 참조 변수

    //XML Pull Paser 시작
    val parkinglotAsyncTask = object : AsyncTask<Void?, Void?, List<ParkinglotInfoItem>>() {
        var urlCnt = 1 //URL 주소 증가용
        override fun doInBackground(vararg voids: Void?): List<ParkinglotInfoItem> {
            val parkinglotInfoArrayList = ArrayList<ParkinglotInfoItem>()
            while (urlCnt <= 8) {
                //요청할 url를 만듦
                requestUrl =
                    "http://openapi.jejuits.go.kr/OPEN_API/pisInfo/getPisInfo?serviceKey=YOUR_SERVICE_KEY&pageNo=${urlCnt}&startPage=1&numOfRows=1&pageSize=10"
                //try를 통해 요청을 시작
                try {
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
                                        itemOfParkinglotInfo = ParkinglotInfoItem()
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
                                        itemOfParkinglotInfo?.saveISTL_LCTN_ADDR(parser.text)
                                        stringISTL_LCTN_ADDR?.add(parser.text)
                                        ISTL_LCTN_ADDR = false
                                    }
                                    UPDT_DT -> {
                                        itemOfParkinglotInfo?.saveUPDT_DT(parser.text)
                                        getUPDT_DTTime = parser.text
                                        itemOfParkinglotInfo?.saveTV_updatedTime(getNetTime())
                                        UPDT_DT = false
                                    }
                                    GNRL_RMND_PRZN_NUM -> {
                                        itemOfParkinglotInfo?.saveGNRL_RMND_PRZN_NUM(parser.text)
                                        stringGNRL_RMND_PRZN_NUM.add(parser.text)
                                        GNRL_RMND_PRZN_NUM = false
                                    }
                                    HNDC_RMND_PRZN_NUM -> {
                                        itemOfParkinglotInfo?.saveHNDC_RMND_PRZN_NUM(parser.text)
                                        stringHNDC_RMND_PRZN_NUM.add(parser.text)
                                        HNDC_RMND_PRZN_NUM = false
                                    }
                                    LGVH_RMND_PRZN_NUM -> {
                                        itemOfParkinglotInfo?.saveLGVH_RMND_PRZN_NUM(parser.text)
                                        LGVH_RMND_PRZN_NUM = false
                                    }
                                    EMVH_RMND_PRZN_NUM -> {
                                        itemOfParkinglotInfo?.saveEMVH_RMND_PRZN_NUM(parser.text)
                                        EMVH_RMND_PRZN_NUM = false
                                    }
                                    HVVH_RMND_PRZN_NUM -> {
                                        itemOfParkinglotInfo?.saveHVVH_RMND_PRZN_NUM(parser.text)
                                        HVVH_RMND_PRZN_NUM = false
                                    }
                                }
                            }
                            XmlPullParser.END_TAG -> {
                                when {
                                    parser.name == "itemList" && itemOfParkinglotInfo != null ->
                                        parkinglotInfoArrayList!!.add(itemOfParkinglotInfo!!)
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
            return parkinglotInfoArrayList
        }

        override fun onPostExecute(s: List<ParkinglotInfoItem>) {
            super.onPostExecute(s)
            when (s.size) {
                in 0..1 -> toast("정보롤 가져오지 못했어요")
                in 3..8 -> {
                    toast("주차장 정보를 불러왔어요")
                    _data?.value = s
                    _getStringISTL_LCTN_ADDR?.value = stringISTL_LCTN_ADDR
                    _getStringGNRL_RMND_PRZN_NUM?.value = stringGNRL_RMND_PRZN_NUM
                    _getStringHNDC_RMND_PRZN_NUM?.value = stringHNDC_RMND_PRZN_NUM
                }
            }
        }
    }.execute()


    /**목록전용
     *
     *
     *
     ***/
    val _data = MutableLiveData<List<ParkinglotInfoItem>>()
    val data: LiveData<List<ParkinglotInfoItem>>
        get() = _data

    /**지도 전용
     *
     *
     *
     * **/
    val _getStringISTL_LCTN_ADDR = MutableLiveData<ArrayList<String>>()
    val getStringISTL_LCTN_ADDR: LiveData<ArrayList<String>>
        get() = _getStringISTL_LCTN_ADDR
    val _getStringGNRL_RMND_PRZN_NUM = MutableLiveData<ArrayList<String>>()
    val getStringGNRL_RMND_PRZN_NUM: LiveData<ArrayList<String>>
        get() = _getStringGNRL_RMND_PRZN_NUM
    val _getStringHNDC_RMND_PRZN_NUM = MutableLiveData<ArrayList<String>>()
    val getStringHNDC_RMND_PRZN_NUM: LiveData<ArrayList<String>>
        get() = _getStringHNDC_RMND_PRZN_NUM
}
