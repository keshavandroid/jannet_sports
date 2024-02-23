package com.e.jannet_stable_code.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.coachfilterdata.CoachFilterResult
import com.e.jannet_stable_code.utils.Utilities

class CoachFilterListAdapter() : RecyclerView.Adapter<CoachFilterListAdapter.ChildListAdapterVH>() {

    var arrayList: List<CoachFilterResult?>? = ArrayList()
    lateinit var context: Activity
    var listner: CoachFilterListAdapter.AdapterListInterface? = null

    constructor(
        arrayList: List<CoachFilterResult?>?,
        context: Activity,
        addChildlistInterface: CoachFilterListAdapter.AdapterListInterface
    ) : this() {
        this.arrayList = arrayList
        this.listner = addChildlistInterface
        this.context = context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoachFilterListAdapter.ChildListAdapterVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_list, parent, false)

        return ChildListAdapterVH(view)
    }

    override fun onBindViewHolder(
        holder: CoachFilterListAdapter.ChildListAdapterVH,
        position: Int
    ) {
        try {
            holder.txtDate.text =
                Utilities.convertDateFormat(arrayList!![position]!!.getEventDate()!!)
            holder.txtEventName.text = arrayList?.get(position)!!.getEventName()

            Glide.with(context)
                .load(arrayList!![position]!!.getImage())
                .into(holder.imgEvent)

        } catch (e: Exception) {
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
        fun onItemSelected(position: Int, data: CoachFilterResult)
    }

}