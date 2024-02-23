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
import com.e.jannet_stable_code.retrofit.gettickitsdata.GetTicketsResullt
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult
import de.hdodenhof.circleimageview.CircleImageView

class TicketsAdapter() : RecyclerView.Adapter<TicketsAdapter.MyViewHolder>() {

    var datalist: List<GetTicketsResullt?> = ArrayList()

    constructor(context: Context, datalist: List<GetTicketsResullt?>) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_get_tickets, parent, false)
        return MyViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: TicketsAdapter.MyViewHolder, position: Int) {

        var currentItem = datalist[position]

        holder.coat.text = currentItem?.getCoat().toString()
        holder.time.text = currentItem?.getTime().toString()
        holder.price.text = currentItem?.getAmount().toString()
        holder.teamAName.text = currentItem?.getTeamAName().toString()
        holder.teamBName.text  = currentItem?.getTeamBName().toString()


        Glide.with(holder.ivTeamAImage.context)
            .load(currentItem?.getTeamAImage())
            .into(holder.ivTeamAImage)


        Glide.with(holder.ivTeamBImage.context)
            .load(currentItem?.getTeamAImage())
            .into(holder.ivTeamBImage)

    }

    override fun getItemCount(): Int {

        return datalist.size

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivTeamAImage = itemView.findViewById<CircleImageView>(R.id.imgTeamA1_tickets)
        val ivTeamBImage = itemView.findViewById<CircleImageView>(R.id.imgTeamB2_tickets)

        val teamAName = itemView.findViewById<TextView>(R.id.team_a_name_tickets)
        val teamBName = itemView.findViewById<TextView>(R.id.tv_team_b_name_tickets)

        val time =  itemView.findViewById<TextView>(R.id.tv_time_tickets)
        val coat =  itemView.findViewById<TextView>(R.id.tv_coat_tickets)
        val price =  itemView.findViewById<TextView>(R.id.tv_price_tickets)

    }

}