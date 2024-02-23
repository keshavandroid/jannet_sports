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
import com.e.jannet_stable_code.retrofit.response.EventListResponse
import com.e.jannet_stable_code.utils.Utilities
import org.w3c.dom.Text


private const val TAG = "EventReqAdapter"

class EventRequestListAdapter() : RecyclerView.Adapter<EventRequestListAdapter.ChildListAdapterVH>() {

    var arrayList: List<CoachEventListResult?>? = ArrayList()
    lateinit var context: Activity
    var listner: EventRequestListAdapter.AdapterListInterface? = null
    var requestType : String = ""
    var status: String = ""


    constructor(
        arrayList: List<CoachEventListResult?>?,
        context: Activity,
        addChildlistInterface:AdapterListInterface,
        requestTypeBooking: String,
        status: String

    ) : this() {
        this.arrayList = arrayList
        this.listner = addChildlistInterface
        this.context = context
        this.requestType = requestTypeBooking
        this.status = status
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListAdapterVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_request, parent, false)

        return ChildListAdapterVH(view)
    }

    override fun onBindViewHolder(holder: ChildListAdapterVH, position: Int) {

        try {

            if(status.equals("0")){
                holder.txtAcceptBtn.visibility = View.VISIBLE
                holder.txtRejectBtn.visibility = View.VISIBLE
            }else if (status.equals("1")){
                holder.txtAcceptBtn.visibility = View.GONE
                holder.txtRejectBtn.visibility = View.GONE
            }else if (status.equals("2")){
                holder.txtAcceptBtn.visibility = View.VISIBLE
                holder.txtRejectBtn.visibility = View.GONE
            }

            if(requestType.equals("ticket")){
                holder.txtEventInfo.text = arrayList?.get(position)?.getUserName() +" requested to 1 match ticket for " +
                        arrayList?.get(position)?.getEventName() +" event, Do you want to allow ?"
            }else if (requestType.equals("event")){
                holder.txtEventInfo.text = arrayList?.get(position)?.getChildName() +" requested to 1 match ticket for " +
                        arrayList?.get(position)?.getEventName() +" event"
            }
/*
            if(requestType.equals("ticket")){
                holder.txtMatchID.visibility = View.VISIBLE
                holder.txtMatchID.setText("Match ID : "+arrayList?.get(position)?.getMatchId())
            }else{
                holder.txtMatchID.visibility = View.GONE
            }*/



            holder.tv_amount_booking_list.text = arrayList?.get(position)?.getFees()

            /*holder.txtDate.text = Utilities.convertDateFormat(arrayList!![position]!!.getEventDate()!!)
            holder.txtEventName.text = arrayList?.get(position)!!.getEventName()

            Glide.with(context)
                .load(arrayList!![position]!!.getImage())
                .into(holder.imgEvent)*/

        }catch (e:Exception){

            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }


    inner class ChildListAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_amount_booking_list: TextView = itemView.findViewById(R.id.tv_amount_booking_list)
        val txtEventInfo: TextView = itemView.findViewById(R.id.txtEventInfo)
        val tv_event_name_booking_list: TextView = itemView.findViewById(R.id.tv_event_name_booking_list)
        private val ll_main_booking_list: LinearLayout = itemView.findViewById(R.id.ll_main_booking_list)
        val txtRejectBtn: TextView = itemView.findViewById(R.id.txtRejectBtn)
        val txtAcceptBtn: TextView = itemView.findViewById(R.id.txtAcceptBtn)
        val txtMatchID: TextView = itemView.findViewById(R.id.txtMatchID)

        init {
            /*ll_main_booking_list.setOnClickListener {
                try {
                    listner!!.onItemSelected(adapterPosition, arrayList!![adapterPosition]!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }*/

            txtAcceptBtn.setOnClickListener {
                try {
                    listner!!.onAcceptClicked(adapterPosition, arrayList!![adapterPosition]!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            txtRejectBtn.setOnClickListener {
                try {
                    listner!!.onRejectClicked(adapterPosition, arrayList!![adapterPosition]!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    interface AdapterListInterface {
        fun onItemSelected(position: Int, data: CoachEventListResult)
        fun onAcceptClicked(position: Int, data: CoachEventListResult)
        fun onRejectClicked(position: Int, data: CoachEventListResult)

    }

}