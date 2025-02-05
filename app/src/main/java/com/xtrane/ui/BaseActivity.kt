package com.xtrane.ui

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.utils.CustomProgressDialog
import java.util.concurrent.atomic.AtomicBoolean

open abstract class BaseActivity : AppCompatActivity() {
    var blockEvents = AtomicBoolean(false)

    abstract fun getController(): IBaseController?

    private var textLoadingDialog: CustomProgressDialog? = null
    private var loadingDialog: CustomProgressDialog? = null

//    private var dialogView: DialogView? = null

    private var blockEventHandler: Handler? = null
    private val blockEventRunnable = Runnable { blockEvents.set(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blockEventHandler = Handler()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        getController()?.onDestroy()
    }

    override fun finish() {
        super.finish()
        getController()?.onFinish()
    }


    open fun blockEvents() {
        if (blockEvents.get()) return
        blockEvents.set(true)
        blockEventHandler!!.removeCallbacks(blockEventRunnable)
        blockEventHandler!!.postDelayed(blockEventRunnable, 600)
    }

    open fun isEventBlocked(): Boolean {
        return blockEvents.get()
    }


    open fun showLoader() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) return
        hideLoader()
        loadingDialog = CustomProgressDialog(this, "")
        try {
            loadingDialog!!.show()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    open fun showLoader(message: String?) {
        if (textLoadingDialog != null && textLoadingDialog!!.isShowing()) return
        hideLoader()
        textLoadingDialog = CustomProgressDialog(this, message)
        textLoadingDialog!!.setMessage(message)
        try {
            textLoadingDialog!!.show()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    open fun hideLoader() {

        if (loadingDialog != null && loadingDialog!!.isShowing())
            try {
                loadingDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace();
            }
        if (textLoadingDialog != null && textLoadingDialog!!.isShowing()) try {
            textLoadingDialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }


    open fun showToast(resId: Int) {
        try {
            Toast.makeText(this, resources.getString(resId), Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
        }
    }

    open fun showToast(message: String?) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
        }
    }


//    open fun showDialog(
//        title: String = "METStech",
//        message: String = "Empty!",
//        positiveButton: String = "OK",
//        negativeButton: String = "Dimiss",
//        listener: DialogView.ButtonListener
//    ) {
//        dialogView?.onDestroyView()
//        dialogView = DialogView(this)
//        dialogView?.setTitle(title)
//        dialogView?.setMessage(message)
//        dialogView?.setListener(listener)
//        dialogView?.setPositiveButtonText(positiveButton)
//        dialogView?.setNegativeButtonText(negativeButton)
//        dialogView?.show()
//    }


    //end


}
