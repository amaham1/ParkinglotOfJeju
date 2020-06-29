package com.jursi.parkinglotofjeju

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.gun0912.tedpermission.TedPermissionResult
import com.tedpark.tedpermission.rx2.TedRx2Permission
import kotlinx.android.synthetic.main.activity_main.*
import splitties.activities.start
import splitties.toast.toast
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    //앱 업데이트 여부 참고용
    private val REQUEST_CODE = 101
    private var mAppUpdateManager: AppUpdateManager? = null
    //프래그먼트 참고용
    private val fragmentOfList = FragmentOfParkingLotList()
    private val fragmentOfMap = FragmentOfParkingLotMap()
    //앱바
    var ab: ActionBar? = null
    //백버튼 컨트롤용
    private var backKeyPressedTime: Long = 0
    //카운트용
    var cntValue = 30
    var cntTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, fragmentOfMap).commit()
        supportFragmentManager.beginTransaction().hide(fragmentOfMap).commit()
        //권한
        getPermission()
        //광고
        getADmob()
        //업데이트 감시용
        getUpdate()
        //툴바
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon = resources.getDrawable(R.drawable.threedot)
        //앱바
        getBottomNavigationBar()
        //카운트용
        /*cntTimer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() {
                cntValue = 0
            }

            override fun onTick(millisUntilFinished: Long) {
                cntValue--
            }
        }*
        cntTimer?.start()*/

    } // onCreate 끝







    override fun onResume() {
        super.onResume()
        mAppUpdateManager!!.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                // 인 앱 업데이트가 이미 실행중이었다면 계속해서 진행하도록
                try {
                    mAppUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@MainActivity,
                        REQUEST_CODE
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                toast("업데이트를 거부했어요\n 원할한 어플 사용을 위해 업데이트를 해주세요")
            }
        }
    }

    //백버튼 컨트롤
    override fun onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis()
            toast("한번 더 누르면 앱을 종료합니다.")
        }
        else{
            finishAffinity()
            exitProcess(0)
        }
    }

    //업데이트 요청
    private fun getUpdate() {
        //업데이트 요청
        mAppUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        // 업데이트 사용 가능 상태인지 체크
        val appUpdateInfoTask: Task<AppUpdateInfo> = mAppUpdateManager!!.appUpdateInfo
        // 사용가능 체크 리스너
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                &&  // 유연한 업데이트 사용 시 (AppUpdateType.FLEXIBLE) 사용
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    mAppUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@MainActivity,
                        REQUEST_CODE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else { }// 업데이트 없으면 다음 액티비티로
        }
    }

    //권한관련
    private fun getPermission() {
        TedRx2Permission.with(this)
            .setRationaleTitle("앱을 이용하려면 권한이 필요해요")
            .setRationaleMessage(" 현재위치 찾기를 위한 위치정보와\n 이메일문의를 위한 이메일 정보를\n 필요로 합니다")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS
            )
            .request()
            .subscribe(
                { tedPermissionResult: TedPermissionResult ->
                    if (tedPermissionResult.isGranted) {
                        toast("권한이 승인 되었어요")
                    } else {
                        toast("${tedPermissionResult.deniedPermissions} 권한이 거부 되었어요. 앱을 종료합니다")
                        finish()
                    }
                },
                { }
            )
    }

    //광고
    private fun getADmob() {
        //광고
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    //바텀 네비게이션바
    private fun getBottomNavigationBar() {
        //하단 네비게이션바 클릭시
        ab = supportActionBar
        ab?.title = "목록"
        val navView: BottomNavigationView = findViewById(R.id.bottom_navi)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.seeWithList -> {
                    supportFragmentManager.beginTransaction().hide(fragmentOfMap).commit()
                    supportFragmentManager.beginTransaction().show(fragmentOfList).commit()
                    ab?.title = "목록"
                }
                R.id.seeWithMap -> {
                    supportFragmentManager.beginTransaction().hide(fragmentOfList).commit()
                    supportFragmentManager.beginTransaction().show(fragmentOfMap).commit()
                    ab?.title = "지도"
                }
            }
            true
        }
    }

    //툴바생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.for_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //툴바 메뉴 선택시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingItem -> {
                start<AppinfoActivity>()
            }
            R.id.sendEmail -> {
                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf("akapwhd@gmail.com"))
                email.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "<${getString(R.string.app_name)} 문의사항>"
                )
                email.type = "*/*"
                startActivity(email)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

