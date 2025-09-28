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
import com.xtrane.R
import com.xtrane.retrofit.mainteamdetaildata.MainTeamParticipentList
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.retrofit.response.EventDetailResponse.coachArray

class CoachListAapter1() : RecyclerView.Adapter<CoachListAapter1.MyViewHolder>() {

    var datalist: List<coachArray?> = ArrayList()

    constructor(context: Context, datalist: List<coachArray?>) : this() {
        this.datalist = datalist

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
        holder.memberName.text = currentItem!!.coachName

        if (currentItem.coachName!=null && currentItem.coachName!!.length>0)
        {
            Glide.with(holder.ivImage.context)
                .load(currentItem.coachName)
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



    }

}