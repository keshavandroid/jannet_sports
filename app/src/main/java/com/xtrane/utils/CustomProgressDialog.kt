package com.xtrane.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.xtrane.R

class CustomProgressDialog(context: Context) : Dialog(context) {

    private var message: String? = ""

    var tvMessage: TextView? = null

    constructor(context: Context, message: String?) : this(context) {
        this.message = message
    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow()?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        setContentView(R.layout.progress_dialog_text)
        tvMessage = findViewById<View>(R.id.txt_massage) as TextView?
        if (message != null && message!!.isNotEmpty()) {
            tvMessage!!.text = message
        } else {
            tvMessage!!.setText(R.string.loading)
        }
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun setDialogCancelable(value: Boolean) {
        setCanceledOnTouchOutside(value)
        setCancelable(value)
    }


    fun setMessage(msg: String?) {
        message = msg
        if (tvMessage != null) {
            tvMessage!!.post { tvMessage!!.text = msg }
        }
    }

    fun getMessage(): String? {
        return tvMessage!!.text.toString()
    }



}