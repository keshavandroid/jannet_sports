package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.BookingDetailAdapter
import com.e.jannet_stable_code.adapter.BookingListAdapter
import com.e.jannet_stable_code.databinding.ActivityAddedMatchlistBinding
import com.e.jannet_stable_code.databinding.ActivityAddedTeamListBinding
import com.e.jannet_stable_code.databinding.ActivityBookingDetail2Binding
import com.e.jannet_stable_code.retrofit.bookingdetaildata.BookingDetailResult
import com.e.jannet_stable_code.retrofit.controller.BooingDetailController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IBookingDetailController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IBookingDetailView


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