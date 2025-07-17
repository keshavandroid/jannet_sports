package com.xtrane.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import com.xtrane.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utilities {


    fun getTimeFromStamp(string: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())
        val msgDateandTime: String = sdf.format(Date(string.toLong()))

        val dateOfProject: Long = parseDateToddMMyyyy(msgDateandTime)
        val dateOfPresent: Long = parseDateToddMMyyyy(currentDateandTime)

        return DateUtils.getRelativeTimeSpanString(
            dateOfProject,
            dateOfPresent,
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    fun convertDateFormat(strDate: String): String {
        try {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "dd MMM yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(strDate)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str!!

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTimeFormat(strDate: String): String {
        try {
            if (strDate.length>0)
            {
                val inputPattern = "HH:mm:ss"  // Use HH for 24-hour format input
                val outputPattern = "hh:mm a"   // Use hh for 12-hour format output with AM/PM
                val inputFormat = SimpleDateFormat(inputPattern)
                val outputFormat = SimpleDateFormat(outputPattern)

                var date: Date? = null
                var str: String? = null

                try {
                    date = inputFormat.parse(strDate)
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                return str!!
            }


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }


    fun changeDateFormat(strDate: String, inputDate: String, outputDate: String): String {
        return try {
            SimpleDateFormat(outputDate).format(SimpleDateFormat(inputDate).parse(strDate))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "changeDateFormat: error>>$strDate>>$inputDate")
            ""
        }
    }

    private fun parseDateToddMMyyyy(time: String?): Long {
        try {
            val sourceFormat =
                SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val parsed = sourceFormat.parse(time) // => Date is in Timezone now
            val tz = TimeZone.getTimeZone("UTC")
            val destFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            destFormat.timeZone = tz
            assert(parsed != null)
            val result = destFormat.format(parsed)
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedfinal = sourceFormat.parse(result)!!
            return parsedfinal.time
        } catch (ignored: java.lang.Exception) {
        }
        return -1
    }

    private const val TAG = "Utilities"
    fun showLog(string1: String, string2: String) {
        Log.d(TAG, "showLog: >>$string1<<     >>$string2<<")
    }

    fun showLog(string1: String) {
        Log.d(TAG, "showLog: >>$string1")
    }

    fun printKeyHash(activity: Activity) {
        /* try {
             val info: PackageInfo = activity.packageManager.getPackageInfo(
                 "com.ramo",
                 PackageManager.GET_SIGNATURES
             )
             for (signature in info.signatures) {
                 val md: MessageDigest = MessageDigest.getInstance("SHA")
                 md.update(signature.toByteArray())
                 Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
             }
         } catch (e: PackageManager.NameNotFoundException) {
         } catch (e: NoSuchAlgorithmException) {
         }*/
    }

    fun isValidEmail(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @SuppressLint("Range")
    fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ", arrayOf(filePath), null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    var progressDialog: Dialog? = null
    fun showProgress(activity: Activity?) {
        if (!isProgressShowing()) {

            progressDialog = Dialog(activity!!)

            progressDialog!!.setCancelable(false)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.progress_dialog)
            try {
                progressDialog!!.show()
            } catch (e: WindowManager.BadTokenException) {
            }
        }
    }

    private fun isProgressShowing(): Boolean {
        return if (progressDialog == null)
            false
        else {
            progressDialog!!.isShowing
        }
    }

    var handlerTop: Handler? = null
    fun dismissProgress() {
        if (handlerTop != null) handlerTop!!.removeCallbacksAndMessages(null)

        handlerTop = Handler()
        handlerTop!!.postDelayed({
            try {
                if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss() else Log.i(
                    "Dialog",
                    "already dismissed"
                )
            } catch (e: Exception) {
            }
            handlerTop = null

        }, 1000)

    }

    fun showToast(activity: Activity?, message: String?) {
        try {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
        }
    }
}