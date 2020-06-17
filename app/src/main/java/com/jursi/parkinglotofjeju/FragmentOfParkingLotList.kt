package com.jursi.parkinglotofjeju

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jursi.parkinglotofjeju.data.ParkinglotInfoItem
import com.jursi.parkinglotofjeju.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_of_parking_lot_list.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer


class FragmentOfParkingLotList : Fragment() {
    private val mParking = ArrayList<ParkinglotInfoItem>()
    private var mRecyclerView: RecyclerView? = null
    private val mParkingAdapter = recyclerViewAdapter(mParking)
    private var parkingViewModel: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_of_parking_lot_list, container, false)
        //리사이클러뷰 지정
        mRecyclerView = rootView.findViewById(R.id.recyclerViewOfMain)
        return rootView
    } //onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_view_container.startShimmerAnimation()

        val context = view.context
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.adapter = mParkingAdapter

    }

    //리사이클러뷰로 보내기
    fun setParkinglot(parking: List<ParkinglotInfoItem>) {
        mParking.clear()
        mParkingAdapter.notifyDataSetChanged()
        for (i in parking) {
            if (!mParking.contains(i)) {
                mParking.add(i)
                mParkingAdapter.notifyItemInserted(mParking.indexOf(i))
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parkingViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        parkingViewModel?.data?.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                setParkinglot(it)
                shimmer_view_container.stopShimmerAnimation()
                shimmer_view_container.visibility = GONE
            }
        })
    }
}

