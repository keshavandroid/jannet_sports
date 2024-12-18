package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.NonParticipentSelectionListAdapter
import com.e.jannet_stable_code.databinding.ActivityAddParticipantBinding
import com.e.jannet_stable_code.databinding.ActivityVenueBinding
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.INonParticipantController
import com.e.jannet_stable_code.retrofit.controller.NonParticipantController
import com.e.jannet_stable_code.retrofit.nonparticipantdata.NonParticipanResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.INonParticipanView


class AddParticipantActivity : BaseActivity(), INonParticipanView {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: INonParticipantController
     var tempList = ArrayList<NonParticipanResult>()
    private lateinit var binding : ActivityAddParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_add_participant)
        binding = ActivityAddParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.participantsListSelection.txtTitle.text = "ADD MEMBER"
        binding.participantsListSelection.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }


        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, "155")
            showLoader()

        binding.txtSelectParticipants.setOnClickListener {

            val intent = Intent(this, AddMemberInTeamActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {


        hideLoader()
         tempList = ArrayList<NonParticipanResult>()
        for (nonParticipanList in response!!) {


            if (nonParticipanList?.getSelected() == true) {

                tempList.add(nonParticipanList)
                Log.e("TAG", "onNonParticipantSuccess: $tempList")
            } else {


            }

        }


        var TeamAdapger = NonParticipentSelectionListAdapter(this, response!!)
        binding.rvParticipantListInTeamSelection.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()


    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }
}