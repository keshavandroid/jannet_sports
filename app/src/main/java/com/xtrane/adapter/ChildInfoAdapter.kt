package com.xtrane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xtrane.R
import com.xtrane.retrofit.childinfodata.ChildInfoResult
import com.xtrane.retrofit.response.GetProfileParentApiResponse

class ChildInfoAdapter() : RecyclerView.Adapter<ChildInfoAdapter.MyViewHolder>() {

    var datalist: List<ChildInfoResult?>? = ArrayList()


    constructor(context: Context, datalist: List<ChildInfoResult?>?) : this() {
        this.datalist = datalist

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_child, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currentItem = datalist!![position]

        holder.tvChildName.text = currentItem?.getName()
        holder.tvChildEmail.text = currentItem?.getEmail()
        holder.tvChildPhone.text = "9998955333"
        if (currentItem?.getChildGender().equals("f")) {

            holder.imgFeMale.setImageResource(R.mipmap.rad)
            holder.imgMale.setImageResource(R.mipmap.rad1)
        } else {
            holder.imgMale.setImageResource(R.mipmap.rad)
            holder.imgFeMale.setImageResource(R.mipmap.rad1)
        }

    //currentItem?.getContactNo() as CharSequence?


}

override fun getItemCount(): Int {
    return datalist!!.size
}


class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvChildName = itemView.findViewById<TextView>(R.id.etxtchildNameInfo)
    val tvChildEmail = itemView.findViewById<TextView>(R.id.etxtChildEmailInfo)
    val tvChildPhone = itemView.findViewById<TextView>(R.id.etxtChildPhoneInfo)
    val imgMale = itemView.findViewById<ImageView>(R.id.imgMale)
    val imgFeMale = itemView.findViewById<ImageView>(R.id.imgFeMale)


}


}