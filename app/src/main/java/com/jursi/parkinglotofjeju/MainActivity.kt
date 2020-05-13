package com.jursi.parkinglotofjeju

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    var parkinglotInfoArrayList: ArrayList<ParkinglotInfo>? = null // ParkinglotInfo 데이터 클래스 리스트 참조용 변수
    var classOfParkinglotInfo: ParkinglotInfo? = null  //ParkinglotInfo 데이터 클래스 참조용 변수
    var recyclerViewOfMain: RecyclerView? = null // RecyclerView 참조용 변수
    var requestUrl: String? = null //API주소 참조용 변수
    var aTpm: ParkinglotParser? = null //ParkinglotParser Asycn 참조용 변수
    var parserAdapter: recyclerViewAdapter? = null //RecyclerViewAdapter 클래스 참조용 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewOfMain = findViewById(R.id.recyclerViewOfMain)
        recyclerViewOfMain?.setHasFixedSize(true)
        //라사이클러뷰 레이아웃매니저 지정
        val lm = LinearLayoutManager(this)
        lm.orientation = LinearLayoutManager.VERTICAL
        recyclerViewOfMain?.layoutManager = lm

        refresh.setOnClickListener {
            //Async 재사용시 초기화 필수
            aTpm = ParkinglotParser()
            aTpm?.execute(100)
        }
    }

    override fun onStart() {
        super.onStart()
        aTpm = ParkinglotParser()
        aTpm?.execute(100)
    }

    //주차장 데이터 읽어오는 부분
    inner class ParkinglotParser : AsyncTask<Int?, Int?, Int?>() {
        val dia =  ProgressDialog(this@MainActivity)

        override fun onPreExecute() {
            super.onPreExecute()

            dia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            dia.setMessage("불러오는 중입니다")
            dia.show()
        }

        var urlCnt = 1 //URL 주소 증가용
        var aTpmProgressCnt = 13
        override fun doInBackground(vararg params: Int?): Int? {

            parkinglotInfoArrayList = ArrayList()
            while (urlCnt <= 8) {
                //요청할 url를 만듦
                requestUrl =
                    "http://openapi.jejuits.go.kr/OPEN_API/pisInfo/getPisInfo?serviceKey=7eF3P0tgu3a8X%2BQzp%2Bj6hmRurphNav%2Bm8orOWMH4zpGUdGD69gHoPQVZfDnH974%2FQQ9h0Ym7feZns1ffaEg8bQ%3D%3D&pageNo=$urlCnt&startPage=1&numOfRows=1&pageSize=10"
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
                                        classOfParkinglotInfo = ParkinglotInfo()
                                        //프로그레스바에 진행도를 넘겨준다
                                        aTpmProgressCnt += 13
                                        publishProgress(aTpmProgressCnt)
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
                                        classOfParkinglotInfo?.saveISTL_LCTN_ADDR(parser.text)
                                        ISTL_LCTN_ADDR = false
                                    }
                                    UPDT_DT -> {
                                        classOfParkinglotInfo?.saveUPDT_DT(parser.text)
                                        UPDT_DT = false
                                    }
                                    GNRL_RMND_PRZN_NUM -> {
                                        classOfParkinglotInfo?.saveGNRL_RMND_PRZN_NUM(parser.text)
                                        GNRL_RMND_PRZN_NUM = false
                                    }
                                    HNDC_RMND_PRZN_NUM -> {
                                        classOfParkinglotInfo?.saveHNDC_RMND_PRZN_NUM(parser.text)
                                        HNDC_RMND_PRZN_NUM = false
                                    }
                                    LGVH_RMND_PRZN_NUM -> {
                                        classOfParkinglotInfo?.saveLGVH_RMND_PRZN_NUM(parser.text)
                                        LGVH_RMND_PRZN_NUM = false
                                    }
                                    EMVH_RMND_PRZN_NUM -> {
                                        classOfParkinglotInfo?.saveEMVH_RMND_PRZN_NUM(parser.text)
                                        EMVH_RMND_PRZN_NUM = false
                                    }
                                    HVVH_RMND_PRZN_NUM -> {
                                        classOfParkinglotInfo?.saveHVVH_RMND_PRZN_NUM(parser.text)
                                        HVVH_RMND_PRZN_NUM = false
                                    }
                                }

                            }
                            XmlPullParser.END_TAG -> {
                                if (parser.name == "itemList" && classOfParkinglotInfo != null) {
                                    parkinglotInfoArrayList!!.add(classOfParkinglotInfo!!)

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
            dia.progress = values[0]!!
        }

        override fun onCancelled() {
            super.onCancelled()
            dia.dismiss()
        }

        override fun onPostExecute(s: Int?) {
            super.onPostExecute(s)
            progressBar?.progress = 0
            dia.dismiss()
            toast("로딩 완료하였습니다")
            parserAdapter = recyclerViewAdapter(applicationContext, parkinglotInfoArrayList!!)
            recyclerViewOfMain?.adapter = parserAdapter
        }
    }

    fun toast(toast: String){
        Toast.makeText(this, toast, LENGTH_LONG).show()
    }

}
