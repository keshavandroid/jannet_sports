package com.e.jannet_stable_code.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.jannet_stable_code.R


public class CustomListSportsDialog(
    var activity: Activity,
    internal var adapter: RecyclerView.Adapter<*>
) : Dialog(activity,R.style.myDialogTheme) {
    private lateinit var listener: DoneInterface
    var dialog: Dialog? = null
    internal var recyclerView: RecyclerView? = null
    internal var llDone: LinearLayout? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mImgSelectAll: ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_sports_dialog_layout)
        recyclerView=findViewById(R.id.recycler_view)

        mImgSelectAll = findViewById(R.id.imgSelectAll)
        llDone = findViewById(R.id.ll_done)

        mLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter
        llDone!!.setOnClickListener(View.OnClickListener { listener!!.onItemSelected() })

        mImgSelectAll?.setOnClickListener(View.OnClickListener { listener!!.onSelectAll() })

        /* yes.setOnClickListener(this)
         no.setOnClickListener(this)*/

    }

    public fun setDoneInterface(listener:DoneInterface) {
    this.listener=listener;
    }

    public interface DoneInterface {
        fun onItemSelected()

        fun onSelectAll()

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