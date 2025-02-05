package com.xtrane.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.ImageView
import com.xtrane.R

object Dialogs {
    fun showJoinLeagueDialog(activity: Activity) {
        /*val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_join_league)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        dialog.show()*/
    }
    fun showImageYesNoDialog(activity: Activity, dialogResp: DialogResp) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        dialogResp.myDialogResponse("yes", "")
                        dialog.dismiss()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialogResp.myDialogResponse("no", "")
                        dialog.dismiss()
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("Cancel", dialogClickListener).show()
    }

    interface DialogResp {
        fun myDialogResponse(string: String, string1: String)
    }
}