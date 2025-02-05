package com.xtrane.ui.coachApp.addEventScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.xtrane.R
import com.xtrane.adapter.NonParticipentSelectionListAdapter
import com.xtrane.databinding.ActivityAddParticipantBinding
import com.xtrane.databinding.ActivityVenueBinding
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.INonParticipanView


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