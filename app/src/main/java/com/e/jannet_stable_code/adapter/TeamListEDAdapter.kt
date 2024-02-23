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
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult

class TeamListEDAdapter() : RecyclerView.Adapter<TeamListEDAdapter.MyViewHolder>() {

    var datalist: List<TeamListResult?> = ArrayList()
    lateinit var iDeleteClickListner: TeamListEDAdapter.IDeleteClickListner
    lateinit var iEditClickListner: TeamListEDAdapter.IEditTeamClickListner
    lateinit var iItemClickListner: TeamListEDAdapter.IItemClickListner


    constructor(context: Context, datalist: List<TeamListResult?>) : this() {
        this.datalist = datalist

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_team_list_event_detail, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = datalist[position]
        holder.teamName.text = currentItem?.getTeamName()

        Glide.with(holder.ivImage.context)
            .load(currentItem?.getImage())
            .into(holder.ivImage)

        holder.ivDelete.setOnClickListener {

            iDeleteClickListner.onDeleteClick(currentItem!!)
        }

        holder.ivEdit.setOnClickListener {

            iEditClickListner.onEditClick(currentItem!!)

        }

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

    interface IDeleteClickListner {

        fun onDeleteClick(deleteTeam: TeamListResult)
    }

    interface IEditTeamClickListner {

        fun onEditClick(editTeam: TeamListResult)
    }


    interface IItemClickListner {

        fun onItemClick(itemCLick:TeamListResult)
    }

}