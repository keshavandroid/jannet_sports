package com.xtrane.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.adapter.MatchListAdapter.IDeleteMatchClickListner
import com.xtrane.retrofit.controller.DeleteMemberController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.matchlistdata.MatchListResult
import com.xtrane.retrofit.nonparticipantdata.GetMemberResult
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult

class MemberListAdapter() : RecyclerView.Adapter<MemberListAdapter.MyViewHolder>(){

    lateinit var iDeleteClickListner: IDeleteTeamMemberClickListner

    var datalist: List<GetMemberResult.MemberResult?> = ArrayList()
    lateinit var controller: DeleteMemberController
    var context: Activity? =null

    constructor(con: Activity, datalist: List<GetMemberResult.MemberResult?>,iDeleteListner:IDeleteTeamMemberClickListner) : this() {
        context=con
        this.datalist = datalist
        iDeleteClickListner=iDeleteListner
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_design_added_participants_in_team, parent, false)
        return MyViewHolder(itemView)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val currentItem = datalist[position]
        holder.memberName.text = currentItem?.getName()

        Glide.with(holder.ivImage.context)
            .load(currentItem?.getImage())
            .into(holder.ivImage)

        holder.iv_delete_participants_from_team.setOnClickListener {

            iDeleteClickListner.deleteMember(currentItem!!)

//            controller = DeleteMemberController(context, this)
//            controller.callDeleteMemberApi(id, token, eventId.toString())

            datalist.toMutableList().removeAt(position)
            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {

       return datalist.size

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage = itemView.findViewById<ImageView>(R.id.ci_added_participant_image)
        val memberName = itemView.findViewById<TextView>(R.id.tv_participant_name)
        val iv_delete_participants_from_team = itemView.findViewById<ImageView>(R.id.iv_delete_participants_from_team)
    }
    interface IDeleteTeamMemberClickListner {

        fun deleteMember(deleteMatch: GetMemberResult.MemberResult)
    }

}