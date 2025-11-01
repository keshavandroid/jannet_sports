package com.xtrane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import com.xtrane.R
import com.xtrane.retrofit.mainteamdetaildata.MainTeamParticipentList
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult

class MainTeamNonParticipantAapter() : RecyclerView.Adapter<MainTeamNonParticipantAapter.MyViewHolder>() {

    var datalist: List<NonParticipanResult?> = ArrayList()
    var flag:String=""

    constructor(context: Context, datalist: List<NonParticipanResult?>,flag1:String) : this() {
        this.datalist = datalist
        flag=flag1

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_main_team_participantlist, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val currentItem = datalist[position]
        holder.memberName.text = currentItem?.getName()
        holder.cb.visibility=View.VISIBLE

        if(flag.equals("y"))
        {
            holder.cb.visibility = View.VISIBLE
        }
        else
        {
            holder.cb.visibility = View.GONE
        }

        if (currentItem!!.getImage()!=null && currentItem.getImage()!!.length>0)
        {
            Glide.with(holder.ivImage.context)
                .load(currentItem.getImage())
                .placeholder(R.mipmap.placeholder)
                .into(holder.ivImage)

        }
        else
        {
            Glide.with(holder.ivImage.context)
                .load(R.mipmap.placeholder)
                .into(holder.ivImage)

        }

    }

    override fun getItemCount(): Int {

        return datalist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<ImageView>(R.id.ci_added_participant_image_not_selected_main)
        val memberName = itemView.findViewById<TextView>(R.id.tv_participant_name_not_selected_main)
        val cb = itemView.findViewById<MaterialCheckBox>(R.id.cb)



    }

}