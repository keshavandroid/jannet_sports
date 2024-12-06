package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.MainTeamListdata.MainTeamListResult
import com.e.jannet_stable_code.retrofit.coachteamdata.CoachTeamResult

class CoachTeamListAdapter() : RecyclerView.Adapter<CoachTeamListAdapter.MyViewHolder>() {

    var datalist: List<CoachTeamResult?> = ArrayList()

    lateinit var iTeamClickListner: ITeamClickListner

    constructor(context: Context, datalist: List<CoachTeamResult?>) : this() {
        this.datalist = datalist

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoachTeamListAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_coach_team_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoachTeamListAdapter.MyViewHolder, position: Int) {
        var currentItem = datalist[position]
        holder.teamName.text = currentItem?.getTeamName()

        Glide.with(holder.ivImage.context)
            .load(currentItem?.getImage())
            .apply(RequestOptions().override(600, 200))
            .placeholder(R.drawable.loader_background)
            .into(holder.ivImage)

        holder.coachTeamType.text = currentItem?.getSportsType().toString()

        holder.itemView.setOnClickListener {

            iTeamClickListner.onTeamClick(currentItem)
        }

    }

    override fun getItemCount(): Int {

        return datalist!!.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<ImageView>(R.id.iv_coach_team_image)
        val teamName = itemView.findViewById<TextView>(R.id.tv_coach_team_name)
        val coachTeamType = itemView.findViewById<TextView>(R.id.tv_coach_team_sports_type)

    }

    interface ITeamClickListner {

        fun onTeamClick(response: CoachTeamResult?)

    }
}