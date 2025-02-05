package com.xtrane.ui.parentsApp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.xtrane.R
import com.xtrane.adapter.EventRequestListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityEventRegisterRequestBinding
import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.controller.AcceptRejectRequestController
import com.xtrane.retrofit.controller.EventRegisterRequestController
import com.xtrane.retrofit.controller.IRegisterEventRequestController
import com.xtrane.utils.CustomProgressDialog
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.viewinterface.IAcceptRejectEventView
import com.xtrane.viewinterface.IEventRegisterRequestView
import com.google.gson.GsonBuilder

class EventRegisterRequestActivity : AppCompatActivity() , IEventRegisterRequestView, IAcceptRejectEventView {

    lateinit var controller: IRegisterEventRequestController
    lateinit var acceptRejectRequestController: AcceptRejectRequestController
    private var loadingDialog: CustomProgressDialog? = null
    private var textLoadingDialog: CustomProgressDialog? = null
    private var id = "";
    private var token = "";
    private var spinnerValue = "";
    private lateinit var binding: ActivityEventRegisterRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_event_register_request)
        binding = ActivityEventRegisterRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


        id = SharedPrefUserData(this).getSavedData().id
        token = SharedPrefUserData(this).getSavedData().token



    }

    private fun init() {
        binding.eventRegisteRequest.imgBack.isVisible = true
        binding.eventRegisteRequest.txtTitle.text = "Event Register Request"
        binding.eventRegisteRequest.txtTitle.isVisible = true

        binding.eventRegisteRequest.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }
        val spinnerEventRequest = findViewById<Spinner>(R.id.spinner_event_request)

        binding.txtEventRequest.setOnClickListener {

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


                binding.txtEventRequest.text = arrayEventRequest[position]

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
        binding.txtNoData.visibility = View.GONE
        binding.rcvEventRequest.visibility = View.VISIBLE
        setListAdapter(response,status)
    }

    private fun setListAdapter(response: List<CoachEventListResult?>?, status: String) {
        binding.rcvEventRequest.adapter = EventRequestListAdapter(
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
            binding.txtNoData.visibility = View.VISIBLE
            binding. rcvEventRequest.visibility = View.GONE
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
}

