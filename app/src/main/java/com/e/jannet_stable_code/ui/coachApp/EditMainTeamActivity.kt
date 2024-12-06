package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.EditTeamController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IEditTeamController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.RegisterData
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IEditTeamView
import com.e.jannet_stable_code.viewinterface.RegisterControllerInterface
import kotlinx.android.synthetic.main.activity_add_main_team.*
import kotlinx.android.synthetic.main.activity_add_teams_final.*
import kotlinx.android.synthetic.main.activity_edit_main_team.*
import kotlinx.android.synthetic.main.topbar_layout.*

class EditMainTeamActivity : BaseActivity(), RegisterControllerInterface {

    lateinit var controller:EditTeamController
    var pickImage: PickImage? = null

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_main_team)


        controller = EditTeamController(this,this)

        txtTitle.text = "EDIT TEAM"
        imgBack.setOnClickListener {

          onBackPressed()
            finish()
        }

        val event_detail = intent.getStringExtra("MAIN_TEAM")
        val teamId = intent.getStringExtra("TEAM_ID")
        val teamName = intent.getStringExtra("TEAM_NAME")
        val teamDescription = intent.getStringExtra("TEAM_DESCRIPTION")
        val teamImage = intent.getStringExtra("TEAM_Image")

        if (event_detail!= null){
            etxtTeamName_addTeam_main_edit.hint = teamName.toString()
            etxt_teams_description_main_edit.hint = teamDescription.toString()

            Glide.with(this)
                .load(teamImage)
                .apply( RequestOptions().override(600, 200))
                .placeholder(R.drawable.loader_background)
                .into(imgProfile_add_team_main_edit)
        }

        txtAdd_teams_event_main_edit.setOnClickListener {

                showLoader()
                val teamId = intent.getStringExtra("TEAM_ID")

                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                var registerData = RegisterData()
                registerData.coach_id = id.toString()
                registerData.teamId = teamId.toString()
                registerData.teamName = etxtTeamName_addTeam_main_edit.text.toString()
                registerData.description = etxt_teams_description_main_edit.text.toString()
                registerData.image = pickImage?.getImage()!!

                controller.EditTeam(registerData)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(
            requestCode,
            resultCode,
            data,
            imgProfile_add_team_main_edit
        )
    }




    override fun <T> onSuccess(response: T) {

        hideLoader()
        showToast("EditTeam Successful")
        onBackPressed()
        finish()
    }

    override fun onFail(error: String?) {

        hideLoader()
        showToast(error)

    }

    override fun <T> onSuccessNew(response: T, method: String) {
        TODO("Not yet implemented")
    }
}