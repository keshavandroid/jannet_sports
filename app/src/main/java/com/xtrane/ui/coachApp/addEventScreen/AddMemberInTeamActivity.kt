package com.xtrane.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xtrane.R
import com.xtrane.adapter.MemberListAdapter
import com.xtrane.databinding.ActivityAddMemberInTeamBinding
import com.xtrane.databinding.ActivityVenueBinding
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.INonParticipanView

class AddMemberInTeamActivity : BaseActivity(), INonParticipanView {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: INonParticipantController
    private lateinit var binding : ActivityAddMemberInTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_add_member_in_team)
        binding = ActivityAddMemberInTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        binding.participantsList.txtTitle.text = "MEMBER LIST"
        binding.participantsList.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }

        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, "155")
        showLoader()
        binding.txtAddParticipants.setOnClickListener {

            val intent = Intent(this, AddParticipantActivity::class.java)
            startActivity(intent)


        }

    }

    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {

        hideLoader()
        var TeamAdapger = MemberListAdapter(this, response!!)
        binding.rvParticipantListInTeam.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()





    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }
}