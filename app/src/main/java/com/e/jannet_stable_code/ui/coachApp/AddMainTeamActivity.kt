package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.AddMainTeamController
import com.e.jannet_stable_code.retrofit.controller.AddTeamController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.RegisterData
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.RegisterControllerInterface
import kotlinx.android.synthetic.main.activity_add_main_team.*
import kotlinx.android.synthetic.main.activity_add_teams_final.*
import kotlinx.android.synthetic.main.topbar_layout.*

class AddMainTeamActivity : BaseActivity(), RegisterControllerInterface {
    lateinit var controller: AddMainTeamController
    var pickImage: PickImage? = null

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_main_team)

        txtTitle.text = "ADD TEAM"
        imgBack.setOnClickListener {

            onBackPressed()
            finish()

        }

        controller = AddMainTeamController(this,this)


        txtAdd_teams_event_main.setOnClickListener {


            if (etxtTeamName_addTeam_main.text.toString() == "") {


                showToast("Please Enter TeamName")

            } else if (etxt_teams_description_main.text.toString() == "") {

                showToast("Please Enter Description")
            } else if (pickImage?.getImage() == null) {

                showToast("Select Image")
            } else {
                showLoader()
                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                var registerData = RegisterData()
                registerData.coach_id = id.toString()
                registerData.teamName = etxtTeamName_addTeam_main.text.toString()
                registerData.description = etxt_teams_description_main.text.toString()
                registerData.image = pickImage?.getImage()!!

                controller.addTeam(registerData)
            }
        }

        imgProfile_add_team_main.setOnClickListener {

            pickImage = PickImage(this)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(
            requestCode,
            resultCode,
            data,
            imgProfile_add_team_main
        )
    }


    override fun <T> onSuccess(response: T) {

        hideLoader()
        showToast("Team Add Successful")
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