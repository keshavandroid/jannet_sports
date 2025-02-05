package com.xtrane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.retrofit.bookingdetaildata.BookedMatchesResult
import com.xtrane.retrofit.bookinglistdata.BookingListResult
import de.hdodenhof.circleimageview.CircleImageView

class BookingDetailAdapter(): RecyclerView.Adapter<BookingDetailAdapter.MyViewHolder>() {


    var datalist: List<BookedMatchesResult?> = ArrayList()


    constructor(context: Context, datalist: List<BookedMatchesResult?>) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingDetailAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_booking_detail, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookingDetailAdapter.MyViewHolder, position: Int) {

        var currentItem = datalist[position]

        holder.coat.text = currentItem?.getCoat().toString()
        holder.time.text = currentItem?.getTime().toString()
        holder.teamAName.text = currentItem?.getTeamAName().toString()
        holder.teamBName.text =currentItem?.getTeamBName().toString()

        Glide.with(holder.ivImageTeamA.context)
            .load(currentItem?.getTeamAImage())
            .into(holder.ivImageTeamA)

        Glide.with(holder.ivImageTeamB.context)
            .load(currentItem?.getTeamBImage())
            .into(holder.ivImageTeamB)


    }

    override fun getItemCount(): Int {

        return  datalist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImageTeamA = itemView.findViewById<CircleImageView>(R.id.imgTeamA_booking_detail)
        val ivImageTeamB = itemView.findViewById<CircleImageView>(R.id.imgTeamB_booking_detail)
        val teamAName = itemView.findViewById<TextView>(R.id.tv_team_a_name_booking_detail)
        val teamBName = itemView.findViewById<TextView>(R.id.tv_team_b_name_booking_detail)
        val time = itemView.findViewById<TextView>(R.id.tv_match_time_booking_detail)
        val coat = itemView.findViewById<TextView>(R.id.tv_coat_booking_detail)

    }

}