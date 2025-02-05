package com.xtrane.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.retrofit.response.SportsListResponse

class SportsGridListAdapter() : RecyclerView.Adapter<SportsGridListAdapter.FreshDailyDealVH>() {

    var arrayList: List<SportsListResponse.Result?> = ArrayList()
    lateinit var context: Activity

    constructor(
        arrayList: List<SportsListResponse.Result?>,
        context: Activity,
    ) : this() {
        this.arrayList = arrayList
        this.context = context
    }

    fun getList():List<SportsListResponse.Result?>{
        return arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreshDailyDealVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sports_grid_list, parent, false)

        return FreshDailyDealVH(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: FreshDailyDealVH, position: Int) {
        holder.txtSportsName.text= arrayList[position]!!.sportsName
        if (arrayList[position]!!.image == null)
            holder.imgSports.setImageResource(R.color.green1)
        else Glide.with(context)
            .load(arrayList[position]!!.image)
            .placeholder(R.color.green1)
            .error(R.color.green1)
            .into(holder.imgSports)

        if(arrayList[position]!!.selected)holder.imgSelected.visibility = View.VISIBLE
        else holder.imgSelected.visibility = View.GONE

        if (arrayList[position]!!.selectedCoach==false){
           holder.imgSelected.visibility = View.GONE
            arrayList[position]!!.selectedCoach=false

        }else{

          holder.imgSelected.visibility = View.VISIBLE
            arrayList[position]!!.selectedCoach=true
        }

    }




    inner class FreshDailyDealVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSportsName: TextView = itemView.findViewById(R.id.txtSportsName)
        val imgSports: ImageView = itemView.findViewById(R.id.imgSports)
        val imgSelected: ImageView = itemView.findViewById(R.id.imgSelected)
        private val clMain: ConstraintLayout = itemView.findViewById(R.id.clMain)

        init {
            clMain.setOnClickListener {
                val flag= arrayList[adapterPosition]!!.selected

                if(flag) {
                    imgSelected.visibility = View.GONE
                    arrayList[adapterPosition]!!.selected=false
                }else{
                    imgSelected.visibility = View.VISIBLE
                    arrayList[adapterPosition]!!.selected=true
                }

            }




        }



    }


}