package com.xtrane.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xtrane.R
import com.xtrane.retrofit.bookinglistdata.BookTicketDetailResult
import com.xtrane.retrofit.bookinglistdata.BookingListResult
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class PBookingListAdapter() : RecyclerView.Adapter<PBookingListAdapter.MyViewHolder>() {
    var datalist: List<BookTicketDetailResult?> = ArrayList()

    lateinit var iItemClickListner: PBookingListAdapter.IitemClickListner

    constructor(context: Context, datalist: List<BookTicketDetailResult?>) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PBookingListAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_detail_view_item, parent, false)
        return MyViewHolder(itemView)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PBookingListAdapter.MyViewHolder, position: Int) {
        var currentItem = datalist[position]

        holder.UName.setText(currentItem?.getuserName())

        holder.tv_price_tickets.setText(currentItem?.getamount())
        holder.tv_num_tik.setText(currentItem?.gettotalTickets())
        if(currentItem!!.getMatchData()!!.size>0){
//            holder.tv_time_tickets.text=
//                currentItem.getMatchData()!!.get(0)!!.getDate()+" "+ currentItem.getMatchData()!!.get(0)!!.getTime()

            holder.tv_time_tickets.text=convertDateFormat(currentItem.getMatchData()!!.get(0)!!.getDate()!!)+" "+ currentItem.getMatchData()!!.get(0)!!.getTime()
            holder.team_a_name_tickets.setText(currentItem.getMatchData()!!.get(0)!!.getTeamAName())
            holder.tv_team_b_name_tickets.setText(currentItem.getMatchData()!!.get(0)!!.getTeamBName())
            holder.tv_coat_tickets.setText(currentItem.getMatchData()!!.get(0)!!.getCoat())
        }



      /*  holder.matches.text = currentItem?.getNoOfMatch().toString()
        holder.tickets.text = currentItem?.getNoOfTicket().toString()
        holder.amount.text = currentItem?.getAmount().toString()

        Glide.with(holder.imgTeamA.context)
            .load(currentItem?.getUserImage())
            .apply(RequestOptions().override(600, 200))
            .placeholder(R.drawable.user)
            .into(holder.imgTeamA)

        holder.llMain.setOnClickListener {
            iItemClickListner.onItemClickListner(currentItem!!)
        }*/
    }

    override fun getItemCount(): Int {

        return datalist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgTeamA = itemView.findViewById<ImageView>(R.id.imgTeamA)
        val imgTeamB = itemView.findViewById<ImageView>(R.id.imgTeamB)
        val UName = itemView.findViewById<TextView>(R.id.UName)
        val tv_time_tickets = itemView.findViewById<TextView>(R.id.tv_time_tickets)
        val team_a_name_tickets = itemView.findViewById<TextView>(R.id.team_a_name_tickets)
        val tv_team_b_name_tickets = itemView.findViewById<TextView>(R.id.tv_team_b_name_tickets)
        val tv_coat_tickets = itemView.findViewById<TextView>(R.id.tv_coat_tickets)
        val tv_num_tik = itemView.findViewById<TextView>(R.id.tv_num_tik)
        val tv_price_tickets = itemView.findViewById<TextView>(R.id.tv_price_tickets)

    }

    interface IitemClickListner {

        fun onItemClickListner(response: BookingListResult)

    }

    fun convertDateFormat(strDate: String): String {
        try {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "dd MMM yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(strDate)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str!!

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

}