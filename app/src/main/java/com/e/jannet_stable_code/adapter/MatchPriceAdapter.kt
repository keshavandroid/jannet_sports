package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult

class MatchPriceAdapter(): RecyclerView.Adapter<MatchPriceAdapter.MyViewHolder>() {

    var datalist: List<MatchListResult?>? = ArrayList()

    constructor(context: Context, datalist: List<MatchListResult?>?) : this() {
        this.datalist = datalist

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchPriceAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_add_match_price, parent, false)
        return MatchPriceAdapter.MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MatchPriceAdapter.MyViewHolder, position: Int) {


        var currentItem = datalist!![position]


        holder.txt_match_coat.text = currentItem?.getCoat()
        holder.txt_match_time.text = currentItem?.getTime()
        holder.txt_team_A_name.text =currentItem?.getTeamAName()
        holder.txt_team_B_name.text =currentItem?.getTeamBName()

        Glide.with(holder.imgTeamA.context)
            .load(currentItem?.getTeamAImage())
            .into(holder.imgTeamA)

        Glide.with(holder.imgTeamB.context)
            .load(currentItem?.getTeamBImage())
            .into(holder.imgTeamB)



    }

    override fun getItemCount(): Int {

        return datalist!!.size

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgTeamA = itemView.findViewById<ImageView>(R.id.imgTeamA_match_price)
        val txt_team_A_name = itemView.findViewById<TextView>(R.id.txt_teamA_name_match_price)
        val txt_match_time = itemView.findViewById<TextView>(R.id.txt_time_add_price)
        val imgTeamB = itemView.findViewById<ImageView>(R.id.imgTeamB_match_price)
        val txt_team_B_name = itemView.findViewById<TextView>(R.id.txt_teamB_name_match_price)
        val txt_match_coat = itemView.findViewById<TextView>(R.id.txt_coat_match_price)

    }
}