package com.xtrane.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager

import java.util.*

fun Intent.getUserType(): String {
    return when {
        getStringExtra(Constants.USER_TYPE).equals(Constants.COACH) -> Constants.COACH
        getStringExtra(Constants.USER_TYPE).equals(Constants.PARTICIPANT) -> Constants.PARTICIPANT
        getStringExtra(Constants.USER_TYPE).equals(Constants.CHILD) -> Constants.CHILD
        getStringExtra(Constants.USER_TYPE).equals(Constants.ADULT) -> Constants.ADULT

        else -> Constants.COACH
    }
}

fun Activity.getPathFromUri(uri: Uri?): String {
    return try {
        ImageFilePath.getPath(this, uri!!)!!
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun datePicker(textView: TextView, activity: Activity,fragmentManager: FragmentManager,inter: DatePickerResult,isChild:Boolean,futureDatesOnly:Boolean) {
    val maxCalendar = Calendar.getInstance()
    if(isChild)
    maxCalendar.add(Calendar.YEAR, -2)
    else
        maxCalendar.add(Calendar.YEAR, -18)
    maxCalendar.add(Calendar.DAY_OF_MONTH,-1)

    val spinnerPickerDialog = SpinnerPickerDialog()

    if(futureDatesOnly)spinnerPickerDialog.futureDatesOnlyFlag=true

    spinnerPickerDialog.context = activity
    spinnerPickerDialog.setMaxCalendar(maxCalendar)
    spinnerPickerDialog.setAllColor(
        ContextCompat.getColor(
            activity,
            android.R.color.holo_green_dark
        )
    )
    spinnerPickerDialog.setmTextColor(Color.BLACK)
    spinnerPickerDialog.setArrowButton(true)
    spinnerPickerDialog.setOnDialogListener(object : SpinnerPickerDialog.OnDialogListener {
        @SuppressLint("SetTextI18n")
        override fun onSetDate(month: Int, day: Int, year: Int) {
            // "  (Month selected is 0 indexed {0 == January})"
            var monthf = month+1
            val monthStr= if((month+1)<10) "0"+(month+1) else monthf.toString()
            val dayStr= if(day<10) "0$day" else day.toString()
            val yearStr= if(year<10) "0$year" else year.toString()



            textView.text = "$yearStr-$monthStr-$dayStr"
            inter.onSuccess("$yearStr-$monthStr-$dayStr")
        }

        override fun onCancel() {}
        override fun onDismiss() {}
    })
        spinnerPickerDialog.show(fragmentManager, "")

    /*val cal = Calendar.getInstance()
    val dialog = DatePickerDialog(
        this,
        R.style.datepicker,
        { _, p1, p2, p3 ->
            var month = (p2 + 1).toString()
            if (month.length == 1) {
                month = "0$month"
            }
            var day = (p3).toString()
            if (day.length == 1) {
                day = "0$day"
            }
            val date = "$day/$month/$p1"
            textView.text = date
            inter.onSuccess(date)
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )
    dialog.datePicker.maxDate = Date().time
    dialog.show()*/
}

interface DatePickerResult {
    fun onSuccess(string: String)
}