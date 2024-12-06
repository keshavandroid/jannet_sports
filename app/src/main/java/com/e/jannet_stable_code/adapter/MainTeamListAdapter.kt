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
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult

class MainTeamListAdapter() : RecyclerView.Adapter<MainTeamListAdapter.MyViewHolder>() {

    var datalist: List<MainTeamListResult?> = ArrayList()
    lateinit var iItemClickListner: MainTeamListAdapter.IItemClickListner


    constructor(context: Context, datalist: List<MainTeamListResult?>) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainTeamListAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_team_list_event_detail, parent, false)
        return MainTeamListAdapter.MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MainTeamListAdapter.MyViewHolder, position: Int) {

        var currentItem = datalist[position]
        holder.teamName.text = currentItem?.getTeamName()

        Glide.with(holder.ivImage.context)
            .load(currentItem?.getImage())
            .apply( RequestOptions().override(600, 200))
            .placeholder(R.drawable.loader_background)
            .into(holder.ivImage)



        holder.itemView.setOnClickListener {

            iItemClickListner.onItemClick(currentItem!!)
        }


    }

    override fun getItemCount(): Int {


        return datalist.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<ImageView>(R.id.iv_team_image_ed)
        val teamName = itemView.findViewById<TextView>(R.id.tv_team_name_event_detail)
        val ivDelete = itemView.findViewById<ImageView>(R.id.iv_delete_team_event_detail)
        val ivEdit = itemView.findViewById<ImageView>(R.id.iv_edit_team_event_detail)

    }



    interface IItemClickListner {

        fun onItemClick(itemCLick:MainTeamListResult)
    }

}