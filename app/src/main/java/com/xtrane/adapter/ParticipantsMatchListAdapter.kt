package com.xtrane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xtrane.R
import com.xtrane.retrofit.matchlistdata.MatchListResult

class ParticipantsMatchListAdapter() : RecyclerView.Adapter<ParticipantsMatchListAdapter.MyViewHolder>() {

    var datalist: List<MatchListResult?>? = ArrayList()

    constructor(context: Context, datalist: List<MatchListResult?>?) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipantsMatchListAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_participent_match_list_ped, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ParticipantsMatchListAdapter.MyViewHolder,
        position: Int
    ) {
        var currentItem = datalist!![position]


        holder.txt_team_A_name.text =currentItem?.getTeamAName().toString()
        holder.txt_team_B_name.text =currentItem?.getTeamBName().toString()
        holder.txt_match_date.text = currentItem?.getDate().toString()
        Glide.with(holder.imgTeamA.context)
            .load(currentItem?.getTeamAImage())
            .apply( RequestOptions().override(600, 200))
            .placeholder(R.drawable.loader_background)
            .into(holder.imgTeamA)

        Glide.with(holder.imgTeamB.context)
            .load(currentItem?.getTeamBImage())
            .apply( RequestOptions().override(600, 200))
            .placeholder(R.drawable.loader_background)
            .into(holder.imgTeamB)




        holder.txt_date.text = currentItem?.getDate().toString()
    }

    override fun getItemCount(): Int {
        return datalist!!.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgTeamA = itemView.findViewById<ImageView>(R.id.iv_team_a_ped)
        val txt_team_A_name = itemView.findViewById<TextView>(R.id.tv_taem_a_Name_ped)
        val txt_match_date = itemView.findViewById<TextView>(R.id.txt_match_date_ped)
        val imgTeamB = itemView.findViewById<ImageView>(R.id.iv_team_b_ped)
        val txt_team_B_name = itemView.findViewById<TextView>(R.id.tv_team_b_name_ped)
        val txt_date = itemView.findViewById<TextView>(R.id.txt_match_date_ped)

    }

}