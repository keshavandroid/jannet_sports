package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import java.text.SimpleDateFormat
import java.util.*

class RegisterListAdapter() : RecyclerView.Adapter<RegisterListAdapter.MyViewHolder>() {
    var datalist: List<CoachEventListResult?>? = ArrayList()
    var type : String = ""

    constructor(context: Context, datalist: ArrayList<CoachEventListResult?>?,type: String) : this() {
        this.datalist = datalist
        this.type = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_register_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currentItem = datalist!![position]

        try {

            if(type.equals("event", ignoreCase = true)){
                val input = SimpleDateFormat("yyyy-MM-dd")
                val dateValue: Date = input.parse(currentItem?.getEventDate())
                val output = SimpleDateFormat("EEE, dd MMM yyyy")

                holder.txtDate.setText(output.format(dateValue))
                holder.txtName.setText(currentItem?.getChildName())
            }else{

                val input = SimpleDateFormat("yyyy-MM-dd")
                val dateValue: Date = input.parse(currentItem?.getEventdate())
                val output = SimpleDateFormat("EEE, dd MMM yyyy")

                holder.txtDate.setText(output.format(dateValue))
                holder.txtName.setText(currentItem?.getUserName())
            }





            holder.tv_amount_booking_list.setText(currentItem?.getFees())
            holder.tv_event_name_booking_list.setText("Event name is " + currentItem?.getEventName() + " and its created by you")
        }catch (e :Exception){
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return datalist!!.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val tv_event_name_booking_list = itemView.findViewById<TextView>(R.id.tv_event_name_booking_list)
        val tv_amount_booking_list = itemView.findViewById<TextView>(R.id.tv_amount_booking_list)


    }



}