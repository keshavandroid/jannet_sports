package com.e.jannet_stable_code.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.ui.parentsApp.PHomeFragment
import kotlinx.android.synthetic.main.dlg_list_item.view.*
import okhttp3.internal.notify

public class CustomListViewDialogAdapter() :
    RecyclerView.Adapter<CustomListViewDialogAdapter.ViewHolder>() {

    var listner: AdapterListInterface? = null
    var mDataset:  List<SportsListResponse.Result?>? = null
    constructor(
        mDataset: List<SportsListResponse.Result?>?,
        listner: AdapterListInterface,
    ) : this() {
        this.mDataset = mDataset
        this.listner = listner

    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.dlg_list_item, parent, false)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.mTextView.text = mDataset!!.get(i)!!.sportsName
        if( mDataset!!.get(i)!!.isCheck.equals("1")){
            holder.iv_check.setImageResource(R.drawable.ic_check)

        }else{
            holder.iv_check.setImageResource(R.color.white)
        }
    }

    override fun getItemCount(): Int {
        return mDataset!!.size
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var mTextView: TextView
        lateinit var main_ll: LinearLayout
        lateinit var iv_check:ImageView

        init {
            mTextView = v.tv_item
            main_ll = v.ll_
            iv_check=v.iv_check
            main_ll.setOnClickListener(View.OnClickListener {
                try {
                    listner!!.onItemSelected(adapterPosition, mDataset!!.get(adapterPosition)!!.sportsName!!)
                    if( mDataset!!.get(adapterPosition)!!.isCheck.equals("1")){
                        iv_check.setImageResource(R.color.white)
                        mDataset!!.get(adapterPosition)!!.isCheck="0"
                    }else{
                        iv_check.setImageResource(R.drawable.ic_check)
                        mDataset!!.get(adapterPosition)!!.isCheck="1"
                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            })

            //  v.setOnClickListener(this)
        }

        /*   @SuppressLint("WrongConstant")
           override fun onClick(v: View) {
               recyclerViewItemClickListener.clickOnItem(mDataset[this.adapterPosition])
               Toast.makeText(v.context,mDataset[this.adapterPosition],1)
           }*/
    }

    interface RecyclerViewItemClickListener {
        fun clickOnItem(data: String)
    }

    public interface AdapterListInterface {
        fun onItemSelected(position: Int, data: String)
    }
}