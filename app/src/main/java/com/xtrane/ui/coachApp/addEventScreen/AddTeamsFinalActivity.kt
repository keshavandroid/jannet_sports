package com.xtrane.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.xtrane.R
import com.xtrane.databinding.ActivityAddTeamsBinding
import com.xtrane.databinding.ActivityAddTeamsFinalBinding
import com.xtrane.retrofit.controller.AddTeamController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.parentsApp.RegisterData
import com.xtrane.ui.parentsApp.TeamsActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.PickImage
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.RegisterControllerInterface

class AddTeamsFinalActivity : BaseActivity(), RegisterControllerInterface {
    lateinit var controller: AddTeamController
    var pickImage: PickImage? = null
    private lateinit var binding : ActivityAddTeamsFinalBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_teams_final)
        binding = ActivityAddTeamsFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = AddTeamController(this, this)

        binding.includeHeader.txtTitle.text = "ADD TEAM"
        binding.includeHeader.imgBack.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Home Screen")
            //set message for alert dialog
            builder.setMessage("Are you Sure you Want to Go Home Screen?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()

                onBackPressed()

            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        binding.txtAddTeamsEvent.setOnClickListener {


            if (binding.etxtTeamNameAddTeam.text.toString() == "") {


                showToast("Please Enter TeamName")

            } else if (binding.etxtTeamsDescription.text.toString() == "") {

                showToast("Please Enter Description")
            } else if (pickImage?.getImage() == null) {

                showToast("Select Image")
            } else {
                showLoader()
                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                val event = intent.getStringExtra("EVENT_ID")
                Log.e("AddTeam", "onCreate:$event ")
                var registerData = RegisterData()
                registerData.event_id = event.toString()
                registerData.coach_id = id.toString()
                registerData.teamName = binding.etxtTeamNameAddTeam.text.toString()
                registerData.description = binding.etxtTeamsDescription.text.toString()
                registerData.image = pickImage?.getImage()!!

                controller.addTeam(registerData)
            }
        }

        binding.imgProfileAddTeam.setOnClickListener {

            pickImage = PickImage(this)


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(
            requestCode,
            resultCode,
            data,
            binding.imgProfileAddTeam
        )
    }

    override fun <T> onSuccess(response: T) {
        hideLoader()
        val event = intent.getStringExtra("EVENT_ID")
        val event_detail = intent.getStringExtra("EVENT_DETAIL")

        if (event_detail?.trim().toString() == "event_detail") {


            val intent = Intent(this, TeamsActivity::class.java)
            intent.putExtra("EVENT_ID", event.toString())
            startActivity(intent)
            finish()
        } else {

            val intent = Intent(this, AddedTeamListActivity::class.java)
            intent.putExtra("EVENT_ID", event.toString())
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

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Home Screen")
        //set message for alert dialog
        builder.setMessage("Are you Sure you Want to Go Home Screen?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()

            super.onBackPressed()


        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


}