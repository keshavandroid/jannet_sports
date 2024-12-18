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
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityEditMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityEditTeamBinding
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


class EditMainTeamActivity : BaseActivity(), RegisterControllerInterface {

    lateinit var controller:EditTeamController
    var pickImage: PickImage? = null
    private lateinit var binding: ActivityEditMainTeamBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_edit_main_team)
        binding = ActivityEditMainTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = EditTeamController(this,this)

        binding.topbar.txtTitle.text = "EDIT TEAM"
        binding.topbar.imgBack.setOnClickListener {

          onBackPressed()
            finish()
        }

        val event_detail = intent.getStringExtra("MAIN_TEAM")
        val teamId = intent.getStringExtra("TEAM_ID")
        val teamName = intent.getStringExtra("TEAM_NAME")
        val teamDescription = intent.getStringExtra("TEAM_DESCRIPTION")
        val teamImage = intent.getStringExtra("TEAM_Image")

        if (event_detail!= null){
            binding.etxtTeamNameAddTeamMainEdit.hint = teamName.toString()
            binding.etxtTeamsDescriptionMainEdit.hint = teamDescription.toString()

            Glide.with(this)
                .load(teamImage)
                .apply( RequestOptions().override(600, 200))
                .placeholder(R.drawable.loader_background)
                .into(binding.imgProfileAddTeamMainEdit)
        }

        binding.txtAddTeamsEventMainEdit.setOnClickListener {

                showLoader()
                val teamId = intent.getStringExtra("TEAM_ID")

                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                var registerData = RegisterData()
                registerData.coach_id = id.toString()
                registerData.teamId = teamId.toString()
                registerData.teamName = binding.etxtTeamNameAddTeamMainEdit.text.toString()
                registerData.description = binding.etxtTeamsDescriptionMainEdit.text.toString()
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
            binding.imgProfileAddTeamMainEdit
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