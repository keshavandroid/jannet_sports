package com.e.jannet_stable_code.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.parentbootomcoach.CoachListResult

class ParentBottomCoachesAdapter() : RecyclerView.Adapter<ParentBottomCoachesAdapter.MyViewHolder>() {

    var datalist: List<CoachListResult?>? = ArrayList()
    lateinit var iCoachClickListner:ICoachClickListner
    lateinit var mContext: Context
    constructor(context: Context, datalist: List<CoachListResult?>?) : this() {
        this.datalist = datalist
        this.mContext = context
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
            .placeholder(R.drawable.loader_background)
            .into(holder.imge)

        holder.name.text = currentItem?.getName().toString()
//        holder.sports.text = "Cricket"

        holder.itemView.setOnClickListener {

            iCoachClickListner.onCoachClick(currentItem!!)
        }


        //FOR SPORTS TYPE
        holder.llSportsType.removeAllViews()

        if (currentItem!!.getSportsname()!=null){

            if (currentItem.getSportsname()!!.isEmpty()){
                holder.sportsTypeLabel.visibility = View.GONE
            }else{
                holder.sportsTypeLabel.visibility = View.VISIBLE
            }

            for (item in currentItem!!.getSportsname()!!) {
                val textView = TextView(mContext).apply {
                    text = item!!.getSportsName()
                    textSize = 18f
                    setPadding(16, 16, 16, 16)
                    setBackgroundColor(Color.parseColor("#F7F6FB")) // Light grey color
                }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16) // Set margins (left, top, right, bottom)
                }
                textView.layoutParams = layoutParams

                holder.llSportsType.addView(textView)
            }
        }else{

            if (currentItem.getSportsName()!!.isEmpty()){
                holder.sportsTypeLabel.visibility = View.GONE
            }else{
                holder.sportsTypeLabel.visibility = View.VISIBLE
            }

            for (item in currentItem!!.getSportsName()!!) {
                val textView = TextView(mContext).apply {
                    text = item!!.getSportsName()
                    textSize = 18f
                    setPadding(16, 16, 16, 16)
                    setBackgroundColor(Color.parseColor("#F7F6FB")) // Light grey color
                }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16) // Set margins (left, top, right, bottom)
                }
                textView.layoutParams = layoutParams

                holder.llSportsType.addView(textView)
            }
        }




    }

    override fun getItemCount(): Int {
        return datalist!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imge = itemView.findViewById<ImageView>(R.id.iv_all_coach_image)
        val name = itemView.findViewById<TextView>(R.id.tv_all_coach_name)
        val sports = itemView.findViewById<TextView>(R.id.tv_all_coach_sports)
        val llSportsType = itemView.findViewById<LinearLayout>(R.id.llSportsType)
        val sportsTypeLabel = itemView.findViewById<TextView>(R.id.sportsTypeLabel)

    }

    interface ICoachClickListner {
        fun onCoachClick(response:CoachListResult?)

    }


}