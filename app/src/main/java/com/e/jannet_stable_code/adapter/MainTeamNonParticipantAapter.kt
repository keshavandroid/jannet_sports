package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.mainteamdetaildata.MainTeamParticipentList
import com.e.jannet_stable_code.retrofit.nonparticipantdata.NonParticipanResult

class MainTeamNonParticipantAapter() :
    RecyclerView.Adapter<MainTeamNonParticipantAapter.MyViewHolder>() {

    var datalist: List<NonParticipanResult?> = ArrayList()

    constructor(context: Context, datalist: List<NonParticipanResult?>) : this() {
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

        var currentItem = datalist[position]
        holder.memberName.text = currentItem?.getName()

        Glide.with(holder.ivImage.context)
            .load(currentItem?.getImage())
            .into(holder.ivImage)

    }

    override fun getItemCount(): Int {

        return datalist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<ImageView>(R.id.ci_added_participant_image_not_selected_main)
        val memberName = itemView.findViewById<TextView>(R.id.tv_participant_name_not_selected_main)



    }

}