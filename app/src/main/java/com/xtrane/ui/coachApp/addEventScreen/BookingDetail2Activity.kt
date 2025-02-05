package com.xtrane.ui.coachApp.addEventScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xtrane.R
import com.xtrane.adapter.BookingDetailAdapter
import com.xtrane.adapter.BookingListAdapter
import com.xtrane.databinding.ActivityAddedMatchlistBinding
import com.xtrane.databinding.ActivityAddedTeamListBinding
import com.xtrane.databinding.ActivityBookingDetail2Binding
import com.xtrane.retrofit.bookingdetaildata.BookingDetailResult
import com.xtrane.retrofit.controller.BooingDetailController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IBookingDetailController
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IBookingDetailView


class BookingDetail2Activity : BaseActivity(), IBookingDetailView {
    private lateinit var bind: ActivityBookingDetail2Binding

    lateinit var controller: IBookingDetailController
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_booking_detail2)
        bind = ActivityBookingDetail2Binding.inflate(layoutInflater)
        setContentView(bind.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        val eventid = intent.getStringExtra("EVENT_ID")
        val book_id = intent.getStringExtra("BOOK_ID")

        bind.topLayout.txtTitle.text = "BOOKING DETAIL"
        bind.topLayout.imgBack.visibility= View.VISIBLE
        bind.topLayout.imgBack.setOnClickListener { finish() }


        controller = BooingDetailController(this, this)
        controller.callBookingDetailApi(id, token,"1" )
        showLoader()
//book_id.toString()
    }

    override fun onFBookingDetailSuccess(response: List<BookingDetailResult?>?) {

        hideLoader()
        val  detail = response!![0]!!.getBookedMatches()
        var BookingListAdapter = BookingDetailAdapter(this,detail!!)
        bind.rvBookingDetail.adapter = BookingListAdapter
        BookingListAdapter.notifyDataSetChanged()


    }

    override fun onFail(message: String?, e: Exception?) {

        showToast(message)
        hideLoader()

    }
}