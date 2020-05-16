package com.jursi.parkinglotofjeju

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_of_parking_lot_map.*


class FragmentOfParkingLotMap : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_of_parking_lot_map, container, false)
        val butt = rootView.findViewById<Button>(R.id.button)

        return rootView
    }

}
