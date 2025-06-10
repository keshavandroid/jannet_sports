package com.xtrane.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xtrane.R
import com.xtrane.retrofit.response.PurchaseHistoryResponse

class PurchaseHistoryAdapter() : RecyclerView.Adapter<PurchaseHistoryAdapter.MyViewHolder>() {


    var arrayList:List<PurchaseHistoryResponse.historyData?>? = ArrayList()
    lateinit var context: Activity

    constructor(
        purarrayList: List<PurchaseHistoryResponse.historyData?>?,
        context: Activity,
    ) : this()
    {
        this.arrayList = purarrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase_history, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currentItem = arrayList!![position]
       holder.tvDate.text = currentItem?.purchasedDate
       holder.purchasedBalance.text = currentItem?.purchasedBalance
//
//        Glide.with(holder.ivImage.context)
//            .load(currentItem?.getImage())
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .into(holder.ivImage)


    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val purchasedBalance = itemView.findViewById<TextView>(R.id.purchasedBalance)

    }
}