package com.e.jannet_stable_code.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult

class EventBookMatchListAdapter() : RecyclerView.Adapter<EventBookMatchListAdapter.MyViewHolder>() {
    var datalist: ArrayList<MatchListResult?>? = ArrayList()
    lateinit var iSelectClickListner: ISelectMatchClickListner


    constructor(context: Context, datalist: ArrayList<MatchListResult?>?) : this() {
        this.datalist!!.addAll(datalist!!)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_book_matchlist_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currentItem = datalist!![position]

        if(currentItem?.getMatchPrice() == null){
            holder.txt_match_price.text = "0"
        }else{
            holder.txt_match_price.text = currentItem?.getMatchPrice()
        }


        holder.txt_match_time.text = currentItem?.getTime()
        holder.txt_team_A_name.text = currentItem?.getTeamAName()
        holder.txt_team_B_name.text = currentItem?.getTeamBName()
        holder.chkPertiMatch.isChecked = currentItem!!.isCheck
        holder.chkPertiMatch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            currentItem.isCheck = isChecked
            iSelectClickListner.selectMatch(datalist!!, isChecked)

        })
        /*   Glide.with(holder.imgTeamA.context)
               .load(currentItem?.getTeamAImage())
               .into(holder.imgTeamA)

           Glide.with(holder.imgTeamB.context)
               .load(currentItem?.getTeamBImage())
               .into(holder.imgTeamB)
   */

        /* holder.deleteMatch.setOnClickListener {
             iSelectClickListner.deletematch(currentItem!!)
         }
 */

    }

    public fun getMatchList() =  datalist

    override fun getItemCount(): Int {

        return datalist!!.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_team_A_name = itemView.findViewById<TextView>(R.id.txt_team_A_name)
        val txt_match_time = itemView.findViewById<TextView>(R.id.txt_match_time)
        val txt_team_B_name = itemView.findViewById<TextView>(R.id.txt_team_B_name)
        val txt_match_price = itemView.findViewById<TextView>(R.id.txt_match_price)
        val chkPertiMatch = itemView.findViewById<CheckBox>(R.id.chkPertiMatch)

    }

    interface ISelectMatchClickListner {
        fun selectMatch(matchList: List<MatchListResult?>, isCheck: Boolean)
    }

}