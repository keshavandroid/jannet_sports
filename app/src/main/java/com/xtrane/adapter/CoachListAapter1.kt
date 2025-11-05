package com.xtrane.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import com.xtrane.R
import com.xtrane.retrofit.response.EventDetailResponse.coachArray

class CoachListAapter1() : RecyclerView.Adapter<CoachListAapter1.MyViewHolder>() {

    var datalist: List<coachArray?> = ArrayList()
    val selectedCoachIds: MutableList<String> = mutableListOf()
    var flag:String=""

    constructor(datalist: List<coachArray?>,flag1:String) : this() {
        this.datalist = datalist
        flag=flag1
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_main_team_participantlist, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val currentItem = datalist[position]
        holder.memberName.text = currentItem!!.coachName

        if(flag.equals("yes"))
        {
         holder.cb.visibility = View.VISIBLE
        }
        else
        {
            holder.cb.visibility = View.GONE
        }

        if (currentItem.coachName!=null && currentItem.coachName!!.length>0)
        {
            Glide.with(holder.ivImage.context)
                .load(currentItem.coachName)
                .placeholder(R.mipmap.placeholder)
                .into(holder.ivImage)
        }
        else
        {
            Glide.with(holder.ivImage.context)
                .load(R.mipmap.placeholder)
                .into(holder.ivImage)

        }

    // When we select checkbox, add value of coach id

    // Assuming 'cb' is the checkbox (currently a TextView) - should be a CheckBox for this logic.
    // If not, this code may need to be adjusted after UI fix.
    // Let's assume 'cb' is actually a CheckBox and update logic for checking selection.

    // First, check if 'cb' is a CheckBox.
        holder.cb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Add value of coach id to selectedCoachIds list
                val coachId = currentItem.coachId
                if (coachId != null) {
                    // Declare selectedCoachIds if not already declared
                    // Place this declaration at the top of your class
                    if (!selectedCoachIds.contains(coachId.toString())) {
                        selectedCoachIds.add(coachId.toString())
                        Log.d("CoachListAapter1", "Coach with id $coachId selected. Current list: $selectedCoachIds")
                    }
                }
            }
            else {
                val coachId = currentItem.coachId
                Log.d("CoachListAapter1", "Coach with id $coachId deselected")
            }
        }

        
    }

    override fun getItemCount(): Int {

        return datalist.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<ImageView>(R.id.ci_added_participant_image_not_selected_main)
        val memberName = itemView.findViewById<TextView>(R.id.tv_participant_name_not_selected_main)
        val cb = itemView.findViewById<MaterialCheckBox>(R.id.cb)

    }

}