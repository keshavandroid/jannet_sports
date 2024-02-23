package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.BookingListAdapter
import com.e.jannet_stable_code.adapter.PBookingListAdapter
import com.e.jannet_stable_code.retrofit.bookinglistdata.BookTicketDetailResult
import com.e.jannet_stable_code.retrofit.bookinglistdata.BookingListResult
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IBokingListView
import com.e.jannet_stable_code.viewinterface.IBookTicketDetailView
import kotlinx.android.synthetic.main.activity_booking_detail.*
import kotlinx.android.synthetic.main.topbar_layout.*

class BookingDetailActivity : BaseActivity(), IBokingListView, IBookTicketDetailView,
    BookingListAdapter.IitemClickListner {
    lateinit var controller: IBookingListController
    lateinit var controller1: IBookTicketDetailController
    var id: String = ""
    var token: String = ""
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)


        txtTitle.text = "BOOKING DETAIL"
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener { finish() }

        var storeData = StoreUserData(this)
        id = storeData.getString(Constants.COACH_ID)
        token = storeData.getString(Constants.COACH_TOKEN)

        val eventid = intent.getStringExtra("EVENT_id")

        if (id.isEmpty()) {

            id = SharedPrefUserData(this).getSavedData().id.toString()
            token = SharedPrefUserData(this).getSavedData().token.toString()
            controller1 = BookTicketDetailController(this, this)
            if (eventid != null) {
                controller1.callBookTicketDetailApi(id, token, eventid)
            }

        } else {
            controller = BookingListController(this, this)
            controller.callBookingDetailApi(id, token, "165")
            showLoader()
//eventid.toString()
        }
    }

    override fun onBookingDetailSuccess(response: List<BookingListResult?>?) {

        hideLoader()
        var storeData = StoreUserData(this)
        if (storeData.getString(Constants.COACH_ID).isEmpty()) {

        } else {
             var BookingListAdapter = BookingListAdapter(this, response!!)
            rv_booking_list.adapter = BookingListAdapter
            BookingListAdapter.iItemClickListner = this
            BookingListAdapter.notifyDataSetChanged()
        }


    }

    override fun onBookTicketDetailSuccessful(response: List<BookTicketDetailResult?>?) {
        var BookingListAdapter = PBookingListAdapter(this, response!!)
        rv_booking_list.adapter = BookingListAdapter
        // PBookingListAdapter.iItemClickListner = this
        BookingListAdapter.notifyDataSetChanged()
    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
    }

    override fun onItemClickListner(response: BookingListResult) {

        val bookid = response.getBookId().toString()
        val eventid = intent.getStringExtra("EVENT_ID")

        Log.e("TAG", "onCreate: ========$bookid")
        val intent = Intent(this, BookingDetail2Activity::class.java)
        intent.putExtra("EVENT_ID", eventid.toString())
        intent.putExtra("BOOK_ID", bookid)
        startActivity(intent)
        finish()


    }
}