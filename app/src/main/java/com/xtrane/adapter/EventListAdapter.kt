package com.xtrane.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.autoimageslider.SliderView
import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.response.EventListResponse
import com.xtrane.retrofit.response.SliderItem
import com.xtrane.utils.Utilities

private const val TAG = "EventListAdapter"

class EventListAdapter() : RecyclerView.Adapter<EventListAdapter.ChildListAdapterVH>() {

    var arrayList: List<CoachEventListResult?>? = ArrayList()
    lateinit var context: Activity
    var listner: AdapterListInterface? = null
    var adapter: SliderAdapterExample? = null

    constructor(
        arrayList: List<CoachEventListResult?>?,
        context: Activity,
        addChildlistInterface: AdapterListInterface
    ) : this() {
        this.arrayList = arrayList
        this.listner = addChildlistInterface
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListAdapterVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_list, parent, false)

        return ChildListAdapterVH(view)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChildListAdapterVH, position: Int) {
        try {
            Log.e("eventsize=", arrayList!!.size.toString());

            holder.txtDate.text = Utilities.convertDateFormat(arrayList!![position]!!.getEventDate()!!) +" "+ Utilities.convertTimeFormat(arrayList!![position]!!.getEventTime()!!)

            holder.txtEventName.text = arrayList?.get(position)!!.getEventName()

            Log.e("eventsize=", arrayList!!.size.toString());
//            Glide.with(context)
//                .load(arrayList!![position]!!.getImage())
//                .into(holder.imgEvent)

            val sliderItem = SliderItem()
            sliderItem.description = arrayList?.get(position)!!.getEventName()!!
            sliderItem.imageUrl = arrayList!![position]!!.getImage()!!

//            var imageUrls: List<String>? = null
//            imageUrls = listOf(sliderItem.imageUrl)
//
//            Log.e("imageurl.size1=", arrayList!!.get(position)!!.getImageId()!!.size.toString());
//
//            for (imgstr in arrayList!!.get(position)!!.getImageId()!!.indices) {
//                imageUrls = listOf(arrayList!!.get(position)!!.getImageId()!!.get(imgstr)?.getImage()!!)
//            }


            val imageIdList = arrayList!![position]?.getImageId()
            val imageUrls: List<String> = buildList {
                add(arrayList!![position]!!.getImage()!!) // Add the extra image manually
                imageIdList?.mapNotNull { it?.getImage() }?.let { addAll(it) }
            }



            Log.e("imageurl.size2=", imageUrls!!.size.toString());
            val adapter = ImageSliderAdapter(imageUrls)
            holder.imgEvent.adapter = adapter


//            if (adapter!!.count > 1) {
//                holder.imgEvent.setAutoCycle(true)
//                holder.imgEvent.setScrollTimeInSec(3)
//                holder.imgEvent.startAutoCycle()
//            } else {
//
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ChildListAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val imgEvent: ViewPager2 = itemView.findViewById(R.id.imgEvent)
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
        fun onItemSelected(position: Int, data: CoachEventListResult)
    }
}