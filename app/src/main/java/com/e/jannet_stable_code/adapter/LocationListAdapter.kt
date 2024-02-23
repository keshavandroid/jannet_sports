package com.e.jannet_stable_code.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.bookinglistdata.BookingListResult
import de.hdodenhof.circleimageview.CircleImageView

class LocationListAdapter() : RecyclerView.Adapter<LocationListAdapter.MyViewHolder>() {
   /* var resultList: ArrayList<Place>? = ArrayList()
    lateinit var iItemClickListner: IitemClickListner
    var list : PlaceAPI?=null*/
    constructor(context: Context) : this() {
       // resultList = list.autocomplete(list.toString())

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*var currentItem = datalist[position]

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
        }*/
    }

    override fun getItemCount(): Int {

        return 5


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_road=itemView.findViewById<TextView>(R.id.tv_road)
        var tv_add=itemView.findViewById<TextView>(R.id.tv_add)
    }

    interface IitemClickListner {

        fun onItemClickListner(response: BookingListResult)

    }
}