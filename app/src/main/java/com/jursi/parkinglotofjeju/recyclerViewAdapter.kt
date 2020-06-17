package com.jursi.parkinglotofjeju

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jursi.parkinglotofjeju.data.ParkinglotInfoItem
import com.jursi.parkinglotofjeju.databinding.ParkinglotitemBinding
import com.ramotion.foldingcell.FoldingCell

class recyclerViewAdapter(private val parkingLotList: List<ParkinglotInfoItem>) :
    RecyclerView.Adapter<recyclerViewAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ParkinglotitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val parking = parkingLotList[position]
        holder.binding.parkingviewModel = parking
        holder.binding.executePendingBindings()
        holder.varFolding_cell.setOnClickListener {
            holder.varFolding_cell.toggle(false)
        }
    }

    override fun getItemCount(): Int {
        return parkingLotList.size
    }

    class Holder(val binding: ParkinglotitemBinding) : RecyclerView.ViewHolder(binding.root) {
        var varFolding_cell = itemView.findViewById<FoldingCell>(R.id.folding_cell)
    }
}