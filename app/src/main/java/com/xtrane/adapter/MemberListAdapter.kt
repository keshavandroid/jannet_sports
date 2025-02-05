package com.xtrane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult

class MemberListAdapter() : RecyclerView.Adapter<MemberListAdapter.MyViewHolder>(){


    var datalist: List<NonParticipanResult?> = ArrayList()

    constructor(context: Context, datalist: List<NonParticipanResult?>) : this() {
        this.datalist = datalist

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_added_participants_in_team, parent, false)
        return MemberListAdapter.MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


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

        val ivImage = itemView.findViewById<ImageView>(R.id.ci_added_participant_image)
        val memberName = itemView.findViewById<TextView>(R.id.tv_participant_name)


    }

}