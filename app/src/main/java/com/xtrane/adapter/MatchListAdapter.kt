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
import com.xtrane.retrofit.matchlistdata.MatchListResult

class MatchListAdapter() : RecyclerView.Adapter<MatchListAdapter.MyViewHolder>() {

    var datalist: List<MatchListResult?>? = ArrayList()
    lateinit var iDeleteClickListner: IDeleteMatchClickListner
    lateinit var iEditClickListner: IEditMatchClickListner


    constructor(context: Context, datalist: List<MatchListResult?>?) : this() {
        this.datalist = datalist

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_added_matchlist, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

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


        holder.deleteMatch.setOnClickListener {
            iDeleteClickListner.deletematch(currentItem!!)
        }

        holder.editMatch.setOnClickListener {

            iEditClickListner.editmatch(currentItem!!)
        }
    }

    override fun getItemCount(): Int {

        return datalist!!.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgTeamA = itemView.findViewById<ImageView>(R.id.imgTeamA)
        val txt_team_A_name = itemView.findViewById<TextView>(R.id.txt_team_A_name)
        val txt_match_time = itemView.findViewById<TextView>(R.id.txt_match_time)
        val imgTeamB = itemView.findViewById<ImageView>(R.id.imgTeamB)
        val txt_team_B_name = itemView.findViewById<TextView>(R.id.txt_team_B_name)
        val txt_match_coat = itemView.findViewById<TextView>(R.id.txt_match_coat)
        val editMatch = itemView.findViewById<ImageView>(R.id.iv_edit_match)
        val deleteMatch = itemView.findViewById<ImageView>(R.id.iv_delete_match)

    }

    interface IDeleteMatchClickListner {

        fun deletematch(deleteMatch:MatchListResult)
    }

    interface IEditMatchClickListner {

        fun editmatch(editMatch:MatchListResult)
    }
}