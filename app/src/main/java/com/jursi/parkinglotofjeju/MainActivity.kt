package com.jursi.parkinglotofjeju

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import splitties.activities.start


class MainActivity : AppCompatActivity() {

    var FragmentOfParkingLotMap12: FragmentOfParkingLotMap? = null
    var FragmentOfParkingLotList12: FragmentOfParkingLotList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGotoList.setOnClickListener {
            /*FragmentOfParkingLotList12= FragmentOfParkingLotList()
            supportFragmentManager.beginTransaction().add(R.id.container, FragmentOfParkingLotList12!!).commit()*/
            start<ActivityOfParkingLotList>()
        }
    }
}
