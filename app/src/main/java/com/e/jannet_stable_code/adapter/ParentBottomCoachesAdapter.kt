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
import com.e.jannet_stable_code.retrofit.parentbootomcoach.CoachListResult

class ParentBottomCoachesAdapter() : RecyclerView.Adapter<ParentBottomCoachesAdapter.MyViewHolder>() {

    var datalist: List<CoachListResult?>? = ArrayList()
lateinit var iCoachClickListner:ICoachClickListner
    constructor(context: Context, datalist: List<CoachListResult?>?) : this() {
        this.datalist = datalist

    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentBottomCoachesAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_bottom_coach_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParentBottomCoachesAdapter.MyViewHolder, position: Int) {

        var currentItem = datalist!![position]

        Glide.with(holder.imge.context)
            .load(currentItem?.getImage())
            .apply( RequestOptions().override(600, 200))
            .placeholder(R.drawable.user)
            .into(holder.imge)

        holder.name.text = currentItem?.getName().toString()
        holder.sports.text = "Cricket"

        holder.itemView.setOnClickListener {

            iCoachClickListner.onCoachClick(currentItem!!)
        }

    }

    override fun getItemCount(): Int {

        return datalist!!.size


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imge = itemView.findViewById<ImageView>(R.id.iv_all_coach_image)
        val name = itemView.findViewById<TextView>(R.id.tv_all_coach_name)
        val sports = itemView.findViewById<TextView>(R.id.tv_all_coach_sports)


    }

    interface ICoachClickListner {

        fun onCoachClick(response:CoachListResult?)

    }


}