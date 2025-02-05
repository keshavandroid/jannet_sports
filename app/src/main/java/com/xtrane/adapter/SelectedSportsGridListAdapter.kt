package com.xtrane.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.retrofit.response.SportsListResponse

class SelectedSportsGridListAdapter() : RecyclerView.Adapter<SelectedSportsGridListAdapter.FreshDailyDealVH>() {

    var arrayList: ArrayList<String> = ArrayList()
    var context: Activity?=null

    constructor(
        context: Activity,
    ) : this() {
        this.context = context
    }

    fun setListData(list:ArrayList<String>){
        arrayList=list;
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreshDailyDealVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_selected_sports_list, parent, false)

        return FreshDailyDealVH(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: FreshDailyDealVH, position: Int) {

        Glide.with(context!!)
            .load(arrayList[position])
            .placeholder(R.color.green1)
            .error(R.color.green1)
            .into(holder.imgSport)

    }


    inner class FreshDailyDealVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSport: ImageView = itemView.findViewById(R.id.imgSport)

        init {
        }
    }

}