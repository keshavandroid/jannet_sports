package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityEditMatchBinding
import com.e.jannet_stable_code.databinding.ActivityEditTeamBinding
import com.e.jannet_stable_code.retrofit.controller.AddTeamController
import com.e.jannet_stable_code.retrofit.controller.EditTeamController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.RegisterData
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.RegisterControllerInterface


class EditTeamActivity : BaseActivity(), RegisterControllerInterface {
    lateinit var controller: EditTeamController
    var pickImage: PickImage? = null
    private lateinit var bind: ActivityEditTeamBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    setContentView(R.layout.activity_edit_team)
        bind = ActivityEditTeamBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val team_desriptiom = intent.getStringExtra("TEAM_DESCRIPTION")
        val team_name = intent.getStringExtra("TEAM_NAME")
        val team_image = intent.getStringExtra("TEAM_Image")

        if (team_desriptiom!=null){
            bind.etxtTeamsDescriptionEdit.hint = team_desriptiom.toString()

        }

        if (team_name!=null){

            bind.etxtTeamNameEditTeam.hint = team_name
        }

//        if (team_image!=null) {
//
            Glide.with(this)
                .load(team_image.toString())
                .placeholder(R.drawable.loader_background)
                .into(bind.imgProfileEditTeam)
//        }
        controller = EditTeamController(this, this)
        bind.topbar.txtTitle.text = "EDIT TEAM"
        bind.topbar.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }

        bind.txteditTeamsEvent.setOnClickListener {


//            if (etxtTeamName_EditTeam.text.toString() == "") {
//
//
//                showToast("Please Enter TeamName")
//
//            } else if (etxt_teams_description_edit.text.toString() == "") {
//
//                showToast("Please Enter Description")
//            } else
            if (pickImage?.getImage() == null) {

                showToast("Select Image")
            } else {
                showLoader()
                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                val teamID = intent.getStringExtra("TEAM_ID")
                Log.e("AddTeam", "onCreate:$teamID ")
                var registerData = RegisterData()
                registerData.teamId = teamID.toString()
                registerData.teamName = bind.etxtTeamNameEditTeam.text.toString()
                registerData.description = bind.etxtTeamsDescriptionEdit.text.toString()
                registerData.image = pickImage?.getImage()!!

                controller.EditTeam(registerData)
            }


        }
        bind.imgProfileEditTeam.setOnClickListener {

            pickImage = PickImage(this)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(
            requestCode,
            resultCode,
            data,
            bind.imgProfileEditTeam
        )
    }


    override fun <T> onSuccess(response: T) {

        val eventId = intent.getStringExtra("EVENT_ID")
        val TEAM_DETAIL = intent.getStringExtra("TEAM_DETAIL")
        val matchID = intent.getStringExtra("TEAM_ID")

        hideLoader()

        if (TEAM_DETAIL?.trim().toString() == "team_detail") {

            val intent = Intent(this, TeamDetailActivity::class.java)
            intent.putExtra("EVENT_ID", eventId.toString())
            intent.putExtra("TEAM_ID", matchID.toString())

            startActivity(intent)
            finish()

        } else {
            val intent = Intent(this, AddedTeamListActivity::class.java)
            intent.putExtra("EVENT_ID", eventId.toString())
            startActivity(intent)
            finish()
        }


    }

    override fun onFail(error: String?) {

        showToast(error)

    }

    override fun <T> onSuccessNew(response: T, method: String) {
        TODO("Not yet implemented")
    }
}