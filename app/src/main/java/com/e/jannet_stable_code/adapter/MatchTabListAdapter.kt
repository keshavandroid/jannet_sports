package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult
import com.e.jannet_stable_code.utils.Utilities
import com.makeramen.roundedimageview.RoundedImageView
import java.lang.Exception

class MatchTabListAdapter() : RecyclerView.Adapter<MatchTabListAdapter.MyViewHolder>() {
    var datalist: List<MatchListResult?>? = ArrayList()
    lateinit var iMatchItemClickClickListner: IMatchItemClickClickListner


    constructor(context: Context, datalist: List<MatchListResult?>?) : this() {
        this.datalist = datalist

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currentItem = datalist!![position]

        try {
            holder.txtDate.setText(Utilities.convertDateFormat(currentItem!!.getDate()!!) + " " + currentItem!!.getTime())

            Glide.with(holder.imgEvent.context)
                    .load(currentItem.getEventImage())
                    .into(holder.imgEvent)

            holder.txtEventName.setText(currentItem.getEventName())

            holder.llMain.setOnClickListener(View.OnClickListener {
                if(iMatchItemClickClickListner!=null)
                    iMatchItemClickClickListner.onClick(currentItem)
            })
        }catch (e: Exception){
            e.printStackTrace()
        }




        /*
        holder.deleteMatch.setOnClickListener {
            iDeleteClickListner.deletematch(currentItem!!)
        }

        holder.editMatch.setOnClickListener {

            iEditClickListner.editmatch(currentItem!!)
        }*/
    }

    override fun getItemCount(): Int {
        return datalist!!.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val llMain = itemView.findViewById<LinearLayout>(R.id.llMain)
        val imgEvent = itemView.findViewById<ImageView>(R.id.imgEvent)
        val txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        val txtEventName = itemView.findViewById<TextView>(R.id.txtEventName)


        /* val txt_team_A_name = itemView.findViewById<TextView>(R.id.txt_team_A_name)
         val txt_match_time = itemView.findViewById<TextView>(R.id.txt_match_time)
         val imgTeamB = itemView.findViewById<ImageView>(R.id.imgTeamB)
         val txt_team_B_name = itemView.findViewById<TextView>(R.id.txt_team_B_name)
         val txt_match_coat = itemView.findViewById<TextView>(R.id.txt_match_coat)
         val editMatch = itemView.findViewById<ImageView>(R.id.iv_edit_match)
         val deleteMatch = itemView.findViewById<ImageView>(R.id.iv_delete_match)*/

    }

   public interface IMatchItemClickClickListner {

        fun onClick(result: MatchListResult)
    }


}