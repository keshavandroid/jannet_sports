package com.e.jannet_stable_code.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.jannet_stable_code.R
import kotlinx.android.synthetic.main.custom_dialog_layout.*

class CustomListViewDialog (var activity: Activity, internal var adapter: RecyclerView.Adapter<*>) : Dialog(activity)
{
    var dialog: Dialog? = null
    private lateinit var listener: DoneInterface
    internal var recyclerView: RecyclerView? = null
    var llDone:LinearLayout?=null
    private var mLayoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog_layout)

        recyclerView = recycler_view
        llDone=ll_done
        mLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter
        llDone!!.setOnClickListener(View.OnClickListener { listener!!.onItemSelected() })

       /* yes.setOnClickListener(this)
        no.setOnClickListener(this)*/

    }
    public fun setDoneInterface(listener: DoneInterface) {
        this.listener=listener;
    }

    public interface DoneInterface {
        fun onItemSelected()
    }

    /*
    override fun onClick(v: View) {
        when (v.id) {
            R.id.yes -> {
            }
            R.id.no -> dismiss()
            else -> {
            }
        }//Do Something
        dismiss()
    }*/
}