package com.e.jannet_stable_code.ui.parentsApp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.EventRequestListAdapter
import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.controller.AcceptRejectRequestController
import com.e.jannet_stable_code.retrofit.controller.EventRegisterRequestController
import com.e.jannet_stable_code.retrofit.controller.IRegisterEventRequestController
import com.e.jannet_stable_code.utils.CustomProgressDialog
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.viewinterface.IAcceptRejectEventView
import com.e.jannet_stable_code.viewinterface.IEventRegisterRequestView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_event_register_request.*
import kotlinx.android.synthetic.main.topbar_layout.*

class EventRegisterRequestActivity : AppCompatActivity() , IEventRegisterRequestView, IAcceptRejectEventView {

    lateinit var controller: IRegisterEventRequestController
    lateinit var acceptRejectRequestController: AcceptRejectRequestController
    private var loadingDialog: CustomProgressDialog? = null
    private var textLoadingDialog: CustomProgressDialog? = null
    private var id = "";
    private var token = "";
    private var spinnerValue = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_register_request)

        init()


        id = SharedPrefUserData(this).getSavedData().id
        token = SharedPrefUserData(this).getSavedData().token



    }

    private fun init() {
        imgBack.isVisible = true
        txtTitle.text = "Event Register Request"
        txtTitle.isVisible = true

        imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }
        val spinnerEventRequest = findViewById<Spinner>(R.id.spinner_event_request)

        txt_event_request.setOnClickListener {

            spinnerEventRequest.performClick()
        }
        val arrayEventRequest = resources.getStringArray(R.array.TicketRequest)

        if (spinnerEventRequest != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, arrayEventRequest
            )
            spinnerEventRequest.adapter = adapter
        }

        spinnerEventRequest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {


                txt_event_request.text = arrayEventRequest[position]

                if (arrayEventRequest[position]=="Current Request"){
                    showLoader()
                    spinnerValue = "0"
                    controller = EventRegisterRequestController(this@EventRegisterRequestActivity, this@EventRegisterRequestActivity)
                    controller.callEventRegisterRequestApi(id, token, "0")
                }else if (arrayEventRequest[position]=="Accepted Request"){
                    showLoader()
                    spinnerValue = "1"
                    controller = EventRegisterRequestController(this@EventRegisterRequestActivity, this@EventRegisterRequestActivity)
                    controller.callEventRegisterRequestApi(id, token, "1")
                }else if (arrayEventRequest[position]=="Rejected Request"){
                    showLoader()
                    spinnerValue = "2"
                    controller = EventRegisterRequestController(this@EventRegisterRequestActivity, this@EventRegisterRequestActivity)
                    controller.callEventRegisterRequestApi(id, token, "2")
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    override fun onEventRegisterRequestSuccess(response: List<CoachEventListResult?>?, status: String) {
        Log.d("onSuccessRes",GsonBuilder().setPrettyPrinting().create().toJson(response))
        txtNoData.visibility = View.GONE
        rcvEventRequest.visibility = View.VISIBLE
        setListAdapter(response,status)
    }

    private fun setListAdapter(response: List<CoachEventListResult?>?, status: String) {
        rcvEventRequest.adapter = EventRequestListAdapter(
            response,
            this,
            object : EventRequestListAdapter.AdapterListInterface {
                override fun onItemSelected(position: Int, data: CoachEventListResult) {

                    /*startActivity(
                        Intent(
                            this@EventRegisterRequestActivity,
                            EventDetailsActivity::class.java
                        ).putExtra("from", "home")
                            .putExtra("eventId", data?.getId().toString())
                            .putExtra("fees", data!!.getFees())
                            .putExtra("parent", "parent")
                    )*/
                    //Log.e("CHome", "==event id====${data?.getId().toString()}")

                }

                override fun onAcceptClicked(position: Int, data: CoachEventListResult) {
                    showLoader()
                    acceptRejectRequestController = AcceptRejectRequestController(this@EventRegisterRequestActivity, this@EventRegisterRequestActivity)
                    acceptRejectRequestController.acceptRejectEvent(id, token, data.getChildId().toString(),data.getId().toString(), "1")
                }

                override fun onRejectClicked(position: Int, data: CoachEventListResult) {
                    showLoader()
                    acceptRejectRequestController = AcceptRejectRequestController(this@EventRegisterRequestActivity, this@EventRegisterRequestActivity)
                    acceptRejectRequestController.acceptRejectEvent(id, token, data.getChildId().toString(),data.getId().toString(), "0")
                }

            },"event",status)
    }

    override fun onAcceptRejectEventSuccess() {
        controller = EventRegisterRequestController(this@EventRegisterRequestActivity, this@EventRegisterRequestActivity)
        controller.callEventRegisterRequestApi(id, token, spinnerValue)
    }

    override fun onAcceptRejectTicketSuccess() {
    }

    override fun showLoader() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) return
        hideLoader()
        loadingDialog = CustomProgressDialog(this, "")
        try {
            loadingDialog!!.show()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    override fun showLoader(message: String?) {
        TODO("Not yet implemented")
    }

    override fun hideLoader() {
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

    override fun onFail(message: String?, e: Exception?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideLoader()

        try {
            txtNoData.visibility = View.VISIBLE
            rcvEventRequest.visibility = View.GONE
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
}

