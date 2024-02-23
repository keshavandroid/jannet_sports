package com.e.jannet_stable_code.ui.parentsApp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.SliderAdapterExample
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.response.EventDetailResponse
import com.e.jannet_stable_code.retrofit.response.GetProfileParentApiResponse
import com.e.jannet_stable_code.retrofit.response.SliderItem
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.CoachMainActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.BookingDetailActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.EditEventActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.TickitsActivityCoach
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.Constants.eventDetailTop
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteEventView
import com.e.jannet_stable_code.viewinterface.IProfileView
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.topbar_layout.*

class EventDetailsActivity : BaseActivity(), IProfileView, IDeleteEventView {
    var adapter: SliderAdapterExample? = null
    var address = ""
    var event_name = ""
    var description = ""
    private var latitude = ""
    private var longitude = ""
    lateinit var eventDetairesponse: EventDetailResponse
    private var id = ""
    private var token = ""
//    var eventResulArraylist: ArrayList<EventDetailResponse.Result> =
//        ArrayList<EventDetailResponse.Result>()


    lateinit var controller: IProfileController
    lateinit var iDeleteEventController: IDeleteEventController
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        setTopBar()

        //use get profile controller for check child is avail or not for hiding register button
        GetProfileController(this, true, object : ControllerInterface {
            override fun onFail(error: String?) {

                hideLoader()
//                showToast(error)
            }

            override fun <T> onSuccess(response: T, method: String) {

                hideLoader()
                try {

                    val data = response as GetProfileParentApiResponse
                    val res = data.getResult()
                    data.getResult()
                    if (res!![0]!!.child!!.isEmpty()) {

                        ll1.isVisible = true
                        txtRegister.isGone = true

                    } else {
                        ll1.isVisible = true
                        txtRegister.isVisible = true


                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })


        iDeleteEventController = DeleteEventController(this, this)
        //        if (intent.getUserType() == Constants.COACH) {
//
//            imgDelete.isVisible = true
//            ll8.isVisible = true
//            ll7.isGone = true
//        }
//        else if (intent.getUserType() == Constants.PARTICIPANT) {
//
//            imgDelete.isGone = true
//            ll8.isGone = true
//            ll7.isVisible = true
//        }
//

//        val id: String = SharedPrefUserData(this).getSavedData().id
//        val token: String = SharedPrefUserData(this).getSavedData().token
//        val userType: String = SharedPrefUserData(this).getSavedData().usertype


        val storedata = StoreUserData(this)

        if (storedata.getString(Constants.COACH_ID)
                .trim() == null || storedata.getString(Constants.COACH_ID).trim()
                .isEmpty() || storedata.getString(Constants.COACH_ID).trim() == ""
        ) {

            id = SharedPrefUserData(this).getSavedData().id
            token = SharedPrefUserData(this).getSavedData().token
            val userType: String = SharedPrefUserData(this).getSavedData().usertype


            controller = ProfileController(this, this)
            controller.callGetProfileAPI(id, token, "parent")
            setDesign(0)
            Log.e(TAG, "onCreate: parent vet detail call")

        } else {


            id = storedata.getString(Constants.COACH_ID)
            token = storedata.getString(Constants.COACH_TOKEN)

            controller = ProfileController(this, this)
            controller.callGetProfileAPI(id, token, "coach")
            setDesign(2)
            Log.e(TAG, "onCreate: coach vet detail call")


        }
//        if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
//                .equals("match")
//        ) {
//            Log.e(TAG, "------from match design")
//            setDesign(1)
//        } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
//                .equals("home")
//        ) {
//
//            Log.e(TAG, "----participent --home")
//            setDesign(0)
//        } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
//                .equals("coachPersonal")
//        ) {
//            Log.e(TAG, "------coach persional design")
//            setDesign(2)
//        }


        adapter = SliderAdapterExample(this)
        imageSlider.setSliderAdapter(adapter!!)

        txtMatch.setOnClickListener {
//            if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
//                    .equals("coachPersonal")
//            ) startActivity(
//                Intent(this, MatchListActivity::class.java).putExtra(
//                    "from",
//                    "coachPersonal"
//                )
//            )
//            else startActivity(
//                Intent(this, MatchListActivity::class.java).putExtra(
//                    "from",
//                    "notCoach"
//                )
//            )
            val eventid = intent.getStringExtra("eventId")


            val intent = Intent(this, MatchListActivity::class.java)
            intent.putExtra("ADDRESS", address.toString())
            intent.putExtra("EVENT_NAME", event_name)
            intent.putExtra("DESCRIPTION", description)
            intent.putExtra("EVENT_ID", eventid.toString())

            startActivity(intent)
        }
        txtTeam.setOnClickListener {

            val eventid = intent.getStringExtra("eventId")

            if (intent.getStringExtra("parent") == "parent") {
                val intent = Intent(this, TeamsActivity::class.java)
                intent.putExtra("EVENT_ID", eventid.toString())
                intent.putExtra("parent", "parent")
                startActivity(intent)
            } else {
                val intent = Intent(this, TeamsActivity::class.java)
                intent.putExtra("EVENT_ID", eventid.toString())
                startActivity(intent)

            }


        }
        txtAbout.setOnClickListener { startActivity(Intent(this, EventAboutActivity::class.java)) }

        txtVenue.setOnClickListener {

            val eventid = intent.getStringExtra("eventId")

            val intent = Intent(this, VenueActivity::class.java)
            intent.putExtra("ADDRESS", address.toString())
            intent.putExtra("EVENT_NAME", event_name)
            intent.putExtra("eventId", eventid)
            intent.putExtra("DESCRIPTION", eventid)
            intent.putExtra(Constants.LATITUDE, latitude)
            intent.putExtra(Constants.LONGITUDE, longitude)

            startActivity(intent)
        }
        txtRegister.setOnClickListener {

            val userType = SharedPrefUserData(this).getSavedData().usertype
            val eventid = intent.getStringExtra("eventId")
            val childId = intent.getStringExtra("childId")

            if (userType.equals("adult")) {
                val intent = Intent(this, BookSignatureActivity::class.java)
                intent.putExtra("eventId", eventid.toString())
                intent.putExtra("ChildId", id)
                intent.putExtra("Fees", eventDetailTop!!.getFees())
                Log.e("ADULTID", childId.toString())
                startActivity(intent)
            } else if (userType.equals("child")) {
                val intent = Intent(this, BookSignatureActivity::class.java)
                intent.putExtra("eventId", eventid.toString())
                intent.putExtra("ChildId", id)
                intent.putExtra("Fees", eventDetailTop!!.getFees())
                startActivity(intent)
                Log.e("CHILDID", childId.toString())
            } else {
                val intent = Intent(this, ParentBookActivity::class.java)
                intent.putExtra("eventId", eventid.toString())
                startActivity(intent)
            }


        }
        txtEdit.setOnClickListener {
//            startActivity(
//                Intent(this, EditEventActivity::class.java
//                )
//            )

            val eventid = intent.getStringExtra("eventId")

            val intent = Intent(this, EditEventActivity::class.java)
            intent.putExtra("EVENT_ID", eventid.toString())
            startActivity(intent)
        }

        txt_Tickets_coach.setOnClickListener {

            val eventid = intent.getStringExtra("eventId")
            val fees = intent.getStringExtra("fees")

            val intent = Intent(this, TickitsActivityCoach::class.java)
            intent.putExtra("EVENT_ID", eventid.toString())
            intent.putExtra("fees", fees.toString())
            startActivity(intent)

        }

        txtBooking_details_coach.setOnClickListener {

            val parent = intent.getStringExtra("parent")

            if (parent?.trim().toString() == "parent") {
                val id = intent.getStringExtra("eventId").toString()
                val intent = Intent(this, BookingDetailActivity::class.java)
                intent.putExtra("EVENT_id", id)
                intent.putExtra("ADDRESS", address.toString())
                intent.putExtra("EVENT_NAME", event_name)
                intent.putExtra("ParentBookDetail", "PBookDetail")
                startActivity(intent)

            } else {

                val eventid = intent.getStringExtra("eventId")

                val intent = Intent(this, BookingDetailActivity::class.java)
                intent.putExtra("ADDRESS", address.toString())
                intent.putExtra("EVENT_NAME", event_name)
                intent.putExtra("EVENT_id", eventid)
                startActivity(intent)

            }


        }

        txtBookEvent.setOnClickListener {

            val EVENT_id = intent.getStringExtra("eventId")
            val fees = intent.getStringExtra("fees")

            val intent = Intent(this, BookeEventActivity::class.java)
            intent.putExtra("EVENT_id", EVENT_id.toString())
            intent.putExtra("fees", fees.toString())
            startActivity(intent)
        }

        tv_delete_event.setOnClickListener {

//            val stordata = StoreUserData(this)
//            val id = stordata.getString(Constants.COACH_ID)
//            val token = stordata.getString(Constants.COACH_TOKEN)
//            val eventid = intent.getStringExtra("eventId")
//
//
//
//
//
//            iDeleteEventController.callDeleteEventApi(id, token, eventid.toString())
//            Log.e(TAG, "onCreate: delet event event id =====$eventid")

            displayPopupDialog()
        }
        /*addNewItem(
            "",
            "https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
        )
        addNewItem(
            "",
            "https://media.istockphoto.com/photos/mature-man-running-online-store-picture-id1281476617?b=1&k=20&m=1281476617&s=170667a&w=0&h=gzcJ_9EIpmpX48hHsh-N2sB9TS_UMrUclhKejsu69uo="
        )
        addNewItem(
            "",
            "https://media.istockphoto.com/photos/global-communication-network-picture-id1271613373?b=1&k=20&m=1271613373&s=170667a&w=0&h=9gddr2ZQgJHEqoH6rK8gyu5cMMb40TpcUHP-ebLbn10="
        )
        addNewItem(
            "",
            "https://media.istockphoto.com/photos/searching-browsing-data-information-network-concept-businessman-using-picture-id1264839512?b=1&k=20&m=1264839512&s=170667a&w=0&h=QbzcI671w7ELHtwSMX2VqWOKRIfbq8xiDsBfkd0f9wo="
        )*/
        Log.d(TAG, "onCreate: test447>>" + intent.getStringExtra("eventId"))
        if (intent.getStringExtra("eventId") != null &&
            !intent.getStringExtra("eventId").equals("null") &&
            !intent.getStringExtra("eventId").equals("")
        ) {
            EventDetailController(this@EventDetailsActivity, object : ControllerInterface {
                override fun onFail(error: String?) {

                    Log.e(TAG, "onFail: fail ==========$error")
                }

                override fun <T> onSuccess(response: T, method: String) {
                    try {

                        val resp = response as EventDetailResponse

                        setData(resp.getResult()!!)

                        address = resp.getResult()!![0]!!.getAddress().toString()
                        event_name = resp.getResult()!![0]!!.getEventName().toString()
                        description = resp.getResult()!![0]!!.getDescription().toString()
                        latitude = resp.getResult()!![0]!!.getLatitude()!!
                        longitude = resp.getResult()!![0]!!.getLongitude()!!

                        eventDetairesponse = resp
                        Log.e(TAG, "onSuccess: event detil success===${resp.getResult()}")

                        Log.e(
                            "EventDetail",
                            "=========main iamge===${
                                resp.getResult()!![0]?.getMainimage().toString()
                            }")
                        Log.e(
                            "EventDetail",
                            "=========images===${resp.getResult()!![0]?.getImages().toString()}")

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).callApi(intent.getStringExtra("eventId")!!)
        }


    }

    private fun setData(result: List<EventDetailResponse.Result?>) {
        try {
            val data = result[0]

            Log.e(TAG, "setData: ===========${data?.getAddress()}")

            eventDetailTop = data

            addNewItem(
                "",
                data?.getMainimage()!!

            )

            for (i in data.getImages()!!.indices) {
                addNewItem(
                    "",
                    data.getImages()!![i]?.getImage()!!
                )
            }

//            ll2.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val TAG = "EventDetailsActivity"
    private fun addNewItem(string1: String, string2: String) {
        val sliderItem = SliderItem()
        sliderItem.description = string1
        sliderItem.imageUrl = string2
        adapter!!.addItem(sliderItem)
//        imageSlider.startAutoCycle()
    }

    private fun setDesign(i: Int) {
        when (i) {
            0 -> {
                //from home screen
                //from parent
                txtRegister.visibility = View.VISIBLE
                view1.visibility = View.GONE
                view2.visibility = View.GONE

                ll2.visibility = View.GONE
                ll6.visibility = View.GONE
                ll7.visibility = View.VISIBLE
                ll9.visibility = View.VISIBLE
                ll8.visibility = View.GONE
                txtEdit.visibility = View.GONE

            }
            1 -> {
                //from match screen
                //from paent scren
                txtRegister.visibility = View.GONE
                view1.visibility = View.VISIBLE
                view2.visibility = View.VISIBLE
                ll2.visibility = View.VISIBLE
                ll6.visibility = View.GONE
                ll7.visibility = View.VISIBLE
                txtEdit.visibility = View.GONE
            }
            2 -> {
                //from match screen
                //from coach side
                txtRegister.visibility = View.GONE
                view1.visibility = View.VISIBLE
                view2.visibility = View.VISIBLE
                ll2.visibility = View.VISIBLE
//                ll2.isVisible=true
                ll6.visibility = View.VISIBLE
                ll8.visibility = View.VISIBLE
                ll7.visibility = View.GONE
                ll9.visibility = View.VISIBLE
                tv_delete_event.visibility = View.VISIBLE

                txtEdit.visibility = View.VISIBLE
            }
        }
    }

    private fun setTopBar() {
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.event_detail)
        imgShare.isVisible = true
        imgShare.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, event_name)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))

        }
    }

    override fun onProfileSuccess(response: List<GetProfileParentApiResponse.Result?>?) {

        val child = response?.get(0)?.child
        response!![0]?.child?.size
        showToast("profile success")
        Log.e("EventDetail", "=========child size===${response!![0]?.child?.size.toString()}")


        Log.e("EventDetail", "=========child size===${child?.size.toString()}")
        if (child?.size == null) {
            Log.e("EventDetail", "=========child size2===${child?.size.toString()}")

            txtRegister.isGone = true

        } else {
            Log.e("EventDetail", "=========child size3===${child?.size.toString()}")

            txtRegister.isVisible = true
        }


    }

    override fun onDeleteEventSuccess() {

        hideLoader()
        val intent = Intent(this, CoachMainActivity::class.java)
        startActivity(intent)
        finish()


    }

    override fun onFail(message: String?, e: Exception?) {

        showToast(message)

    }

    private fun displayPopupDialog() {

        var popupDialog = Dialog(this)
        popupDialog.setCancelable(false)

        popupDialog.setContentView(R.layout.event_delete_popup_layout)
        popupDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var solo = popupDialog.findViewById<Button>(R.id.btn_solo)
        var group = popupDialog.findViewById<Button>(R.id.btn_group)



        solo.setOnClickListener {

            val stordata = StoreUserData(this)
            val id = stordata.getString(Constants.COACH_ID)
            val token = stordata.getString(Constants.COACH_TOKEN)
            val eventid = intent.getStringExtra("eventId")

            iDeleteEventController.callDeleteEventApi(id, token, eventid.toString())
            Log.e(TAG, "onCreate: delet event event id =====$eventid")

            showLoader()
            popupDialog.dismiss()
        }


        group.setOnClickListener {
            popupDialog.dismiss()
        }

        popupDialog.show()

    }


}