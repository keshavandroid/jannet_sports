package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.bookinglistdata.BookingListResult
import de.hdodenhof.circleimageview.CircleImageView

class BookingListAdapter(): RecyclerView.Adapter<BookingListAdapter.MyViewHolder>() {

    var datalist: List<BookingListResult?> = ArrayList()

    lateinit var iItemClickListner:IitemClickListner

    constructor(context: Context, datalist: List<BookingListResult?>) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingListAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_booking_list, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: BookingListAdapter.MyViewHolder, position: Int) {
        var currentItem = datalist[position]

        holder.userName.text = currentItem?.getUserName().toString()
        holder.matches.text = currentItem?.getNoOfMatch().toString()
        holder.tickets.text = currentItem?.getNoOfTicket().toString()
        holder.amount.text = currentItem?.getAmount().toString()

        Glide.with(holder.ivImage.context)
            .load(currentItem?.getUserImage())
            .apply( RequestOptions().override(600, 200))
            .placeholder(R.drawable.user)
            .into(holder.ivImage)

        holder.llMain.setOnClickListener {
            iItemClickListner.onItemClickListner(currentItem!!)
        }
    }

    override fun getItemCount(): Int {

        return datalist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<CircleImageView>(R.id.iv_image_booking_list)
        val userName = itemView.findViewById<TextView>(R.id.tv_user_name_booking_list)
        val matches = itemView.findViewById<TextView>(R.id.tv_matches_booking_list)
        val tickets = itemView.findViewById<TextView>(R.id.tv_tickets_bokking_list)
        val amount = itemView.findViewById<TextView>(R.id.tv_amount_booking_list)
        val llMain = itemView.findViewById<LinearLayout>(R.id.ll_main_booking_list)

    }

    interface IitemClickListner {

        fun  onItemClickListner(response:BookingListResult)

    }






}