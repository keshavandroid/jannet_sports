package com.e.jannet_stable_code.ui.parentsApp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.mainteamdetaildata.MainTeamDetailResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.EditMainTeamActivity
import com.e.jannet_stable_code.ui.coachApp.ParticipantsListActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteTeamView
import com.e.jannet_stable_code.viewinterface.IEditTeamView
import com.e.jannet_stable_code.viewinterface.IMainTeamDetailView
import kotlinx.android.synthetic.main.activity_team_details.*
import kotlinx.android.synthetic.main.topbar_layout.*

class TeamDetailsActivity : BaseActivity(), IMainTeamDetailView, IDeleteTeamView, IEditTeamView {

    lateinit var coachTeamDetailController: IMainTeamDetailController
    lateinit var deleteMainTeamController: IDeleteTeamController
    lateinit var editMainTeamController: IEditTeamController
    lateinit var mainTeamImage: String
    lateinit var mainTeamDetail: String
    lateinit var mainTeamName: String
    var mainTeamIdd: String = ""
    override fun getController(): IBaseController? {

        return null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val mainTeamId = intent.getStringExtra("TEAM_ID")

        val coachdetail = intent.getStringExtra("COACHDETAIL")
        val coachdetailId = intent.getStringExtra("CoachID")
        deleteMainTeamController = DeleteTeamCOntroller(this, this)

        coachTeamDetailController = MainTeamDetailController(this, this)

        if (coachdetail?.trim().toString() == "coachdetail") {

            iv_main_team_edit.isGone = true
            iv_main_team_delete.isGone = true
            tv_main_team_name.text = intent.getStringExtra("TEAMNAME").toString()
            tv_main_team_coach_name.text = intent.getStringExtra("COACHNAME").toString()
            tv_main_team_sports.text = intent.getStringExtra("SPORTSTYPE").toString()
            tv_main_teamdescription.text = intent.getStringExtra("DESCRIPTION").toString()
            tv_main_team_fees.text = intent.getStringExtra("FEES").toString()
            val join = intent.getStringExtra("isJoin")
            val image = intent.getStringExtra("IMAGE")
            Glide.with(this)
                .load(image)
                .into(iv_main_team_image)
            if (join?.trim().toString()=="1"){
                txtJoin.isGone = true

            }else{
                txtJoin.isVisible = true

            }

        } else {


            coachTeamDetailController.callMainTeamDetailApi(id, token, mainTeamId.toString())
            showLoader()
        }



        setTopBar()

        iv_main_team_delete.setOnClickListener {

            if (coachdetail?.trim().toString() == "coachdetail") {

            } else {

                displayPopupDialog()

            }

        }

        iv_main_team_edit.setOnClickListener {

            if (coachdetail?.trim().toString() == "coachdetail") {

                //from coach detail

            } else {

                val intent = Intent(this, EditMainTeamActivity::class.java)
                intent.putExtra("TEAM_ID", mainTeamIdd.toString())
                intent.putExtra("TEAM_NAME", mainTeamName.toString())
                intent.putExtra("TEAM_DESCRIPTION", mainTeamDetail.toString())
                intent.putExtra("TEAM_Image", mainTeamImage.toString())
                intent.putExtra("MAIN_TEAM", "main_team")
                startActivity(intent)

            }
        }
//        if(intent.getStringExtra("from")!=null && intent.getStringExtra("from").equals("allcoaches")){
//            txtJoin.visibility=View.GONE
//            txtExpire.visibility=View.VISIBLE
//            txtExpireDate.visibility=View.VISIBLE
//            llEdit.visibility=View.GONE
//            txtCoachName.visibility=View.VISIBLE
//            llLocation.visibility=View.GONE
//            llPerson.visibility=View.GONE
//            txtParticipant.visibility=View.GONE
//        }else if(intent.getStringExtra("from")!=null && intent.getStringExtra("from").equals("coachesdetail")){
//            txtJoin.visibility=View.VISIBLE
//            txtExpire.visibility=View.GONE
//            txtExpireDate.visibility=View.GONE
//            llEdit.visibility=View.GONE
//            txtCoachName.visibility=View.VISIBLE
//            llLocation.visibility=View.GONE
//            llPerson.visibility=View.GONE
//            txtParticipant.visibility=View.GONE
//        }else if(intent.getStringExtra("from")!=null && intent.getStringExtra("from").equals("coachTeamDetail")){
//            txtJoin.visibility=View.GONE
//            txtExpire.visibility=View.GONE
//            txtExpireDate.visibility=View.GONE
//            llEdit.visibility=View.VISIBLE
//            txtCoachName.visibility=View.GONE
//            llLocation.visibility=View.VISIBLE
//            llPerson.visibility=View.VISIBLE
//            txtParticipant.visibility=View.VISIBLE
//        }
        txtParticipant.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ParticipantsListActivity::class.java
                )
            )
        }
        txtJoin.setOnClickListener {
            val coach_id = intent.getStringExtra("CoachID")
            val teamId = intent.getStringExtra("team_id")
            val intent = Intent(this, BookSignatureActivity::class.java)
            intent.putExtra("COACH_JOIN", "coach_join")
            intent.putExtra("Team_id", teamId.toString())
            intent.putExtra("Coach_id", coach_id.toString())
            intent.putExtra("fees", tv_main_team_fees.text.trim().toString())
            startActivity(intent)
        }
    }

    private fun setTopBar() {
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.team_detail)
    }

    override fun onMianTeamDetailSuccessful(response: List<MainTeamDetailResult?>?) {

        hideLoader()
        Glide.with(this)
            .load(response!![0]!!.getImage())
            .apply(RequestOptions().override(600, 200))
            .placeholder(R.drawable.loader_background)
            .into(iv_main_team_image)

        tv_main_team_name.text = response[0]!!.getTeamName().toString()
        tv_main_team_no_participant.text = response[0]!!.getParticipants()!!.size.toString()
        tv_main_teamdescription.text = response[0]!!.getDescription().toString()
        tv_main_team_coach_name.text = response[0]!!.getCoachName().toString()

        mainTeamDetail = response[0]!!.getDescription().toString()
        mainTeamName = response[0]!!.getTeamName().toString()
        mainTeamImage = response!![0]!!.getImage().toString()
        mainTeamIdd = response!![0]!!.getTeamId().toString()


    }

    override fun deleteTeamSuccessful() {

        hideLoader()
        showToast("Delete Team Successful")
        onBackPressed()
        finish()

    }

    override fun onEditTeamSuccessful() {
        TODO("Not yet implemented")
    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
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
            val mainTeamId = intent.getStringExtra("TEAM_ID")





            deleteMainTeamController.callDeleteTeamApi(id, token, mainTeamId.toString())
            showLoader()
            popupDialog.dismiss()
        }


        group.setOnClickListener {
            popupDialog.dismiss()
        }

        popupDialog.show()

    }

}