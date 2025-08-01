package com.xtrane.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.response.EventListResponse
import com.xtrane.utils.Utilities


private const val TAG = "EventListAdapter"

class ParticipentListAdapter() : RecyclerView.Adapter<ParticipentListAdapter.ChildListAdapterVH>() {

    var arrayList: List<EventListResponse.Result?>? = ArrayList()
    lateinit var context: Activity
    var listner: ParticipentListAdapter.AdapterListInterface? = null

    constructor(
        arrayList: List<EventListResponse.Result?>?,
        context: Activity,
        addChildlistInterface:AdapterListInterface
    ) : this() {
        this.arrayList = arrayList
        this.listner = addChildlistInterface
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListAdapterVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participant_event_list, parent, false)

        return ChildListAdapterVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChildListAdapterVH, position: Int) {

        try {
            holder.txtDate.text = Utilities.convertDateFormat(arrayList!![position]!!.getEventDate()!!)+" "+
                    Utilities.convertTimeFormat(arrayList!![position]!!.getEventTime()!!)

            holder.txtEventName.text = arrayList?.get(position)!!.getEventName()

            Glide.with(context)
                .load(arrayList!![position]!!.getImage())
                .into(holder.imgEvent)

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }


    inner class ChildListAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val imgEvent: ImageView = itemView.findViewById(R.id.imgEvent)
        val txtEventName: TextView = itemView.findViewById(R.id.txtEventName)
        private val llMain: LinearLayout = itemView.findViewById(R.id.llMain)

        init {
            llMain.setOnClickListener {
                try {
                    listner!!.onItemSelected(adapterPosition, arrayList!![adapterPosition]!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    interface AdapterListInterface {
        fun onItemSelected(position: Int, data: EventListResponse.Result?)
    }

}