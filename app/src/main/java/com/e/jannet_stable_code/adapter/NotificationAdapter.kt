package com.e.jannet_stable_code.adapter

import android.content.Context
import android.util.Log
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
import com.e.jannet_stable_code.retrofit.notifications.NotificationResult
import java.lang.Exception

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>()  {

    var datalist: List<NotificationResult?>? = ArrayList()
//    lateinit var iDeleteClickListner: NotificationAdapter.IDeleteMatchClickListner
//    lateinit var iEditClickListner: NotificationAdapter.IEditMatchClickListner

    constructor(context: Context, datalist: List<NotificationResult?>?) : this() {
        this.datalist = datalist

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {

        val currentItem = datalist!![position]

        Log.d("dataaa",""+currentItem?.getMessage().toString())

        try {
            holder.txtUser.text = "User : " + currentItem?.getUsername().toString()
            holder.txtMessage.text = currentItem?.getMessage().toString()

            Glide.with(holder.imgProfileUser.context)
                    .load(currentItem?.getUserImage())
                    .apply(RequestOptions().override(600, 200))
                    .placeholder(R.drawable.loader_background)
                    .into(holder.imgProfileUser)

        }catch (e: Exception){
            e.printStackTrace()
        }





    }

    override fun getItemCount(): Int {

        return datalist!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgProfileUser = itemView.findViewById<ImageView>(R.id.imgProfileUser)
        val txtUser = itemView.findViewById<TextView>(R.id.txtUser)
        val txtMessage = itemView.findViewById<TextView>(R.id.txtMessage)



    }




}