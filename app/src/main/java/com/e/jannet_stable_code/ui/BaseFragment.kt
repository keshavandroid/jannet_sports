package com.e.jannet_stable_code.ui

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.utils.CustomProgressDialog

open abstract class BaseFragment:Fragment() {


    private var textLoadingDialog: CustomProgressDialog? = null
    private var loadingDialog: CustomProgressDialog? = null





    open fun showLoader() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) return
        hideLoader()
        loadingDialog = CustomProgressDialog(requireContext(), "")
        try {
            loadingDialog!!.show()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    open fun showLoader(message: String?) {
        if (textLoadingDialog != null && textLoadingDialog!!.isShowing()) return
        hideLoader()
        textLoadingDialog = CustomProgressDialog(requireContext(), message)
        textLoadingDialog!!.setMessage(message)
        try {
            textLoadingDialog!!.show()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    open fun hideLoader() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) try {
            loadingDialog!!.dismiss()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
        if (textLoadingDialog != null && textLoadingDialog!!.isShowing()) try {
            textLoadingDialog!!.dismiss()
        } catch (e: Exception) {
            // e.printStackTrace();
        }
    }

    open fun showToast(message: String?) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
        }
    }


}