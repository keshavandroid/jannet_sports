package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.retrofit.controller.AddMainTeamController
import com.e.jannet_stable_code.retrofit.controller.AddTeamController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.RegisterData
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.RegisterControllerInterface

class AddMainTeamActivity : BaseActivity(), RegisterControllerInterface {
    lateinit var controller: AddMainTeamController
    var pickImage: PickImage? = null
    private lateinit var binding: ActivityAddMainTeamBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_main_team)
        binding = ActivityAddMainTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.incheader.txtTitle.text = "ADD TEAM"
        binding.incheader.imgBack.setOnClickListener {

            onBackPressed()
            finish()

        }

        controller = AddMainTeamController(this,this)


        binding.txtAddTeamsEventMain.setOnClickListener {


            if (binding.txtAddTeamsEventMain.text.toString() == "") {

                showToast("Please Enter TeamName")

            } else if (binding.etxtTeamsDescriptionMain.text.toString() == "") {

                showToast("Please Enter Description")
            } else if (pickImage?.getImage() == null) {

                showToast("Select Image")
            } else {
                showLoader()
                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                var registerData = RegisterData()
                registerData.coach_id = id.toString()
                registerData.teamName = binding.txtAddTeamsEventMain.text.toString()
                registerData.description = binding.etxtTeamsDescriptionMain.text.toString()
                registerData.image = pickImage?.getImage()!!

                controller.addTeam(registerData)
            }
        }

        binding.imgProfileAddTeamMain.setOnClickListener {
            pickImage = PickImage(this)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(
            requestCode,
            resultCode,
            data,
            binding.imgProfileAddTeamMain
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