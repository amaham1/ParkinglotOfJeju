package com.jursi.parkinglotofjeju

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recyclerViewAdapter(private val context: Context, private val parkingLotList: ArrayList<ParkinglotInfo>) :
    RecyclerView.Adapter<recyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.parkinglotitem, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return parkingLotList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.ISTL_LCTN_ADDR.text = parkingLotList[position].ISTL_LCTN_ADDR
        holder.UPDT_DT.text = parkingLotList[position].UPDT_DT
        holder.GNRL_RMND_PRZN_NUM.text = parkingLotList[position].GNRL_RMND_PRZN_NUM
        holder.HNDC_RMND_PRZN_NUM.text = parkingLotList[position].HNDC_RMND_PRZN_NUM
        holder.LGVH_RMND_PRZN_NUM.text = parkingLotList[position].LGVH_RMND_PRZN_NUM
        holder.EMVH_RMND_PRZN_NUM.text = parkingLotList[position].EMVH_RMND_PRZN_NUM
        holder.HVVH_RMND_PRZN_NUM.text = parkingLotList[position].HVVH_RMND_PRZN_NUM

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ISTL_LCTN_ADDR = itemView.findViewById<TextView>(R.id.TV_ISTL_LCTN_ADDR)
        var UPDT_DT = itemView.findViewById<TextView>(R.id.TV_UPDT_DT)
        var GNRL_RMND_PRZN_NUM = itemView.findViewById<TextView>(R.id.TV_GNRL_RMND_PRZN_NUM)
        var HNDC_RMND_PRZN_NUM = itemView.findViewById<TextView>(R.id.TV_HNDC_RMND_PRZN_NUM)
        var LGVH_RMND_PRZN_NUM = itemView.findViewById<TextView>(R.id.TV_LGVH_RMND_PRZN_NUM)
        var EMVH_RMND_PRZN_NUM = itemView.findViewById<TextView>(R.id.TV_EMVH_RMND_PRZN_NUM)
        var HVVH_RMND_PRZN_NUM = itemView.findViewById<TextView>(R.id.TV_HVVH_RMND_PRZN_NUM)

    }

}