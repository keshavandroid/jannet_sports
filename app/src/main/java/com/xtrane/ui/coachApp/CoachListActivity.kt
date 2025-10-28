package com.xtrane.ui.coachApp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xtrane.R
import com.xtrane.adapter.CoachListAapter1
import com.xtrane.databinding.ActivityParticipantsListBinding
import com.xtrane.retrofit.coachteamdata.CoachTeamResult
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachNumberSelectionController
import com.xtrane.retrofit.controller.SelectCoachNumberController
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
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
        if (intent.hasExtra("eventdetailresponse")) {
            eventdata = intent.getSerializableExtra("eventdetailresponse") as EventDetailResponse
            Log.e("eventdata=", eventdata.getResult()!!.size.toString())
        }

        setTopBar()

        for (i in 0 until eventdata.getResult()!!.size) {
            val coachadpter = CoachListAapter1(
                eventdata.getResult()!![i]!!.getCoachArray()!!
            )
            binding.rvParticipantListInTeamMain.adapter = coachadpter
        }
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

            controller.callCoachNumberListApi(id, token, eventId.toString(), selectedCoachIdsString)
        }
    }

    private fun setTopBar() {
        binding.participantsListSelection.imgBack.visibility = View.VISIBLE
        binding.participantsListSelection.imgBack.setOnClickListener { finish() }
        binding.participantsListSelection.txtTitle.text = getString(R.string.coach)
    }

    override fun onCoachTeamListSuccess(response: List<CoachTeamResult?>?) {
        Log.e(ContentValues.TAG, "onCoachTeamListSuccess:"+response.toString())
    }

    override fun onFail(message: String?, e: Exception?) {
        Log.e(ContentValues.TAG, "onFail:"+message.toString())
    }

}