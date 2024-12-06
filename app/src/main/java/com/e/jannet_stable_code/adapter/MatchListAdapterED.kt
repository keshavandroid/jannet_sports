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
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult

class MatchListAdapterED() : RecyclerView.Adapter<MatchListAdapterED.MyViewHolder>()  {

    var datalist: List<MatchListResult?>? = ArrayList()
    lateinit var iDeleteClickListner: MatchListAdapterED.IDeleteMatchClickListner
    lateinit var iEditClickListner: MatchListAdapterED.IEditMatchClickListner

    constructor(context: Context, datalist: List<MatchListResult?>?) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchListAdapterED.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_match_list_event_detail, parent, false)
        return MatchListAdapterED.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {

        var currentItem = datalist!![position]


        holder.txt_team_A_name.text =currentItem?.getTeamAName().toString()
        holder.txt_team_B_name.text =currentItem?.getTeamBName().toString()
        holder.txt_match_date.text = currentItem?.getDate().toString()
        holder.coat.text = currentItem?.getCoat().toString()
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


        holder.deleteMatch.setOnClickListener {
            iDeleteClickListner.deletematch(currentItem!!)
        }

        holder.editMatch.setOnClickListener {

            iEditClickListner.editmatch(currentItem!!)
        }

        holder.txt_date.text = currentItem?.getDate().toString()

    }

    override fun getItemCount(): Int {

        return datalist!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgTeamA = itemView.findViewById<ImageView>(R.id.iv_team_a_ed)
        val txt_team_A_name = itemView.findViewById<TextView>(R.id.tv_taem_a_Name_ed)
        val txt_match_date = itemView.findViewById<TextView>(R.id.txt_match_date_ed)
        val imgTeamB = itemView.findViewById<ImageView>(R.id.iv_team_b_ed)
        val txt_team_B_name = itemView.findViewById<TextView>(R.id.tv_team_b_name_ed)
        val editMatch = itemView.findViewById<ImageView>(R.id.iv_edit_match_event_detail)
        val deleteMatch = itemView.findViewById<ImageView>(R.id.iv_delete_match_event_detail)
        val txt_date = itemView.findViewById<TextView>(R.id.txt_match_date_ed)
        val coat = itemView.findViewById<TextView>(R.id.txt_match_coat_ed)

    }

    interface IDeleteMatchClickListner {

        fun deletematch(deleteMatch:MatchListResult)
    }

    interface IEditMatchClickListner {

        fun editmatch(editMatch:MatchListResult)
    }


}