package com.xtrane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult


class NonParticipentSelectionListAdapter() :
    RecyclerView.Adapter<NonParticipentSelectionListAdapter.MyViewHolder>() {

    var datalist: List<NonParticipanResult?> = ArrayList()
    lateinit var iCheckBocClickListner:ISelectCheckBoxListner

    constructor(context: Context, datalist: List<NonParticipanResult?>,iCheckBocClick:ISelectCheckBoxListner) : this() {
        this.datalist = datalist
        iCheckBocClickListner=iCheckBocClick
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_non_participent_selection, parent, false)
        return MyViewHolder(itemView)


    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        var currentItem = datalist[position]
        holder.memberName.text = currentItem?.getName()

        if (currentItem!!.getImage()!=null && currentItem.getImage()!!.length>0)
        {
            Glide.with(holder.ivImage.context)
                .load(currentItem.getImage())
                .into(holder.ivImage)
        }

        holder.ll.setOnClickListener {

            if (currentItem.getSelected() == true) {
                holder.cb.setImageResource(R.drawable.check150)
                currentItem.setSelected(false)
                iCheckBocClickListner.onCheckBoxClick(currentItem.getId().toString(),false)
            }
            else {
                holder.cb.setImageResource(R.drawable.unchecked_15)
                currentItem.setSelected(true)
                iCheckBocClickListner.onCheckBoxClick(currentItem.getId().toString(),true)
            }

        }

//        holder.cb.setOnClickListener {
//
//
//
//
//            //checkbox click event handling
//            //checkbox click event handling
//            holder.cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//                if (isChecked) {
//
//
//
//                } else {
//                }
//            })
//        }

    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage = itemView.findViewById<ImageView>(R.id.ci_added_participant_image_not_selected)
        val memberName = itemView.findViewById<TextView>(R.id.tv_participant_name_not_selected)

        val cb = itemView.findViewById<ImageView>(R.id.cb)
        val ll = itemView.findViewById<LinearLayout>(R.id.ll)


    }

    interface ISelectCheckBoxListner {

        fun onCheckBoxClick(memberId:String,isSelected:Boolean)
    }



}