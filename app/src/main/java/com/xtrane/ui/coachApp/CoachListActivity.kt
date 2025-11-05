package com.xtrane.ui.coachApp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.xtrane.R
import com.xtrane.adapter.CoachListAapter1
import com.xtrane.databinding.ActivityParticipantsListBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.coachteamdata.CoachTeamResult
import com.xtrane.retrofit.controller.EventDetailController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachNumberSelectionController
import com.xtrane.retrofit.controller.SelectCoachNumberController
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.Constants.eventDetailTop
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.ICoachTeamListView


class CoachListActivity : BaseActivity(), ICoachTeamListView {

    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: ICoachNumberSelectionController
    private lateinit var binding: ActivityParticipantsListBinding
    var eventId: String? = null
    lateinit var eventdata: EventDetailResponse
    var showbtn: String? = "no"
    var TAG: String? = "CoachList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_participants_list)

        binding = ActivityParticipantsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        if (intent.hasExtra("eventId")) {
            eventId = intent.getStringExtra("eventId")
            Log.e("eventID", "======$eventId")
        }
        if (intent.hasExtra("showbtn")) {
            showbtn = intent.getStringExtra("showbtn")
            Log.e("showbtn", "======$showbtn")
        }
        if (intent.hasExtra("eventdetailresponse")) {
            eventdata = intent.getSerializableExtra("eventdetailresponse") as EventDetailResponse
            Log.e("eventdata=", eventdata.getResult()!!.size.toString())
            eventId=eventdata.getResult()!![0]!!.getId().toString()
        }

//        if (intent.hasExtra("eventId")) {
//            eventId = intent.getStringExtra("eventId")
//            Log.e("eventId=", eventId.toString())
//        }

        setTopBar()
        binding.txtTimer.visibility = View.GONE

        if (showbtn.equals("yes")) {
            binding.txtCreateTeam.visibility = View.VISIBLE
        } else {
            binding.txtCreateTeam.visibility = View.GONE

        }
        getEventDetails()


        controller = SelectCoachNumberController(this, this)

        binding.txtCreateTeam.setOnClickListener {
            // Get selected coach IDs from the adapter (assuming only one adapter instance is being used)
            var selectedCoachIds: List<String> = emptyList()
            val adapter = binding.rvParticipantListInTeamMain.adapter

            if (adapter is CoachListAapter1) {
                // Access the selectedCoachIds property from the adapter
                selectedCoachIds = adapter.selectedCoachIds
            }
            // Now selectedCoachIds contains the chosen coach IDs and can be sent or logged
            Log.e("CoachListActivity", "Selected Coach IDs: $selectedCoachIds")
            // Combine all selectedCoachIds into a comma-separated string
            val selectedCoachIdsString = selectedCoachIds.joinToString(separator = ",")
            Log.e("CoachListActivity", "Comma separated coach IDs: $selectedCoachIdsString")

            controller.callCoachNumberListApi(
                id,
                token,
                eventdata.getResult()!![0]!!.getId().toString(),
                selectedCoachIdsString
            )
        }
    }

    private fun setTopBar() {
        binding.participantsListSelection.imgBack.visibility = View.VISIBLE
        binding.participantsListSelection.imgBack.setOnClickListener { finish() }
        binding.participantsListSelection.txtTitle.text = getString(R.string.coach)
    }

    override fun onCoachTeamListSuccess(response: List<CoachTeamResult?>?) {
        Log.e(ContentValues.TAG, "onCoachTeamListSuccess:" + response.toString())
        Toast.makeText(this, "Order set successfully!!", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onFail(message: String?, e: Exception?) {
        Log.e(ContentValues.TAG, "onFail:" + message.toString())
    }

    private fun getEventDetails() {
        Log.e(TAG, "getEventDetails ==========$eventId")

        if (eventId != null &&
            !eventId.equals("null") &&
            !eventId.equals("")
        ) {
            EventDetailController(this@CoachListActivity, object : ControllerInterface {
                override fun onFail(error: String?) {

                    Log.e(TAG, "onFail: fail ==========$error")
                }

                override fun <T> onSuccess(response: T, method: String) {
                    try {

                        val resp = response as EventDetailResponse

                        //setData(resp.getResult()!!)

                        eventdata = resp

                        Log.e(TAG, "onSuccess: event detil success===${resp.getResult()}")

                        Log.e(
                            "EventDetail",
                            "=========main iamge===${
                                resp.getResult()!![0]?.getMainimage().toString()
                            }"
                        )
                        for (i in 0 until eventdata.getResult()!!.size) {
                            val coachadpter = CoachListAapter1(
                                eventdata.getResult()!![i]!!.getCoachArray()!!, showbtn!!
                            )
                            binding.rvParticipantListInTeamMain.adapter = coachadpter
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).callApi(eventId!!)
        }

    }
}