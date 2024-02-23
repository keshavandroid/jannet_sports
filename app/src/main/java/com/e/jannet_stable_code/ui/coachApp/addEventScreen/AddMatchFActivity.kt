package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.MatchListActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IAddMatchView
import com.e.jannet_stable_code.viewinterface.ILocationView
import com.e.jannet_stable_code.viewinterface.ITeamListView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_add_match_f.*
import kotlinx.android.synthetic.main.topbar_layout.*
import java.util.*


class AddMatchFActivity : BaseActivity(), ITeamListView, IAddMatchView, ILocationView {

    lateinit var teamListController: ITeamListController
    lateinit var addMatchController: IAddMatchController
    lateinit var locationController: ILocationController
    var locationId = "0"

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match_f)


        txtSelectTeam_B.setOnClickListener {

            spinnervs2.performClick()
        }
        txtSelectTeam_A.setOnClickListener {

            spinnervs1.performClick()
        }



        txt_spinnerCoat_add.setOnClickListener {
            spinnerCoat_add.performClick()
        }

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")
        Log.e("AddMatch", "event id ==$event_id ")
        addMatchController = AddMatchController(this, this)
        teamListController = TeamListController(this, this)
        teamListController.callTeamLostApi(id, token, event_id.toString())
        locationController = LocationController(this, this)
        locationController.callLocationApi(id, token)

        showLoader()

        txtTitle.text = "ADD Match"
        imgBack.setOnClickListener {
            onBackPressed()
        }


        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                etxtMatchTime.setText(String.format("%d : %d", hourOfDay, minute))
            }
        }, hour, minute, true)


        etxtMatchTime.setOnClickListener({ v ->
            mTimePicker.show()
        })


        txtAdd_Fmatch_event.setOnClickListener {

//            var selectedSpinnerAItem = spinnervs1.selectedItemId
            var selectedSpinnerBItem = spinnervs2.selectedItem as TeamListResult

            var selectedTeamAItem = spinnervs1.selectedItem as TeamListResult


            Log.e("TAG", "===A==$selectedTeamAItem======")
            Log.e("TAG", "===B==$selectedSpinnerBItem======")


            if (selectedTeamAItem.getTeamId() == selectedSpinnerBItem.getTeamId()) {


                showToast("Can't Create Match With Same Team!!")
            } else if (etxtMatchTime.text.toString() == "" && etxtMatchTime.text.toString() == null) {

                showToast("Please Select Match Time")
            } else if (txt_spinnerCoat_add.text.toString() == "" && txt_spinnerCoat_add.text.toString() == null) {

                showToast("Please Select Coat ")

            } else {

                //SharedPrefUserData(this).getSavedData().id
                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                val token = stordata.getString(Constants.COACH_TOKEN)

                addMatchController.callAddMatchApi(
                        id,
                        token,
                        event_id.toString(),
                        id,
                        etxtMatchTime.text.trim().toString().filter { !it.isWhitespace() },
                        txt_spinnerCoat_add.text.toString(),
                        selectedTeamAItem.getTeamId().toString(),
                        selectedSpinnerBItem.getTeamId().toString()
                )
            }

        }

        val CoatList = resources.getStringArray(R.array.Coat)


    }

    override fun onTeamListSuccess(teamList: List<TeamListResult?>?) {


        var tempATypeList = ArrayList<TeamListResult?>()
        var lisAtHint = TeamListResult()
        lisAtHint.setTeamName("Select Team A")
        tempATypeList.add(0, lisAtHint)
        tempATypeList.addAll(teamList!!)


        var tempBTypeList = ArrayList<TeamListResult?>()
        var lisBtHint = TeamListResult()
        lisBtHint.setTeamName("Select Team B")
        tempBTypeList.add(0, lisBtHint)
        tempBTypeList.addAll(teamList!!)


        val arrayAdapterTeamA =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempATypeList!!)
        arrayAdapterTeamA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnervs1.adapter = arrayAdapterTeamA


        spinnervs1.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
            ) {

                var selectedTeamAItem = spinnervs1.selectedItem as TeamListResult
                Log.d(
                        "TAG",
                        "onItemSelected: " + selectedTeamAItem.getTeamId() + "  " + selectedTeamAItem.getTeamName()
                )
                txtSelectTeam_A.text = selectedTeamAItem.getTeamName().toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        val arrayAdapterTeamB =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempBTypeList)
        arrayAdapterTeamB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnervs2.adapter = arrayAdapterTeamB




        spinnervs2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
            ) {

                var selectedTeamBItem = spinnervs2.selectedItem as TeamListResult
                Log.d(
                        "TAG",
                        "onItemSelected: " + selectedTeamBItem.getTeamId() + "  " + selectedTeamBItem.getTeamName()
                )
                txtSelectTeam_B.text = selectedTeamBItem.getTeamName().toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        hideLoader()

    }

    override fun addMatchSuccessful() {


        hideLoader()
        val event_id = intent.getStringExtra("EVENT_ID")
        val event_detail = intent.getStringExtra("EVENT_DETAIL")


        showToast("Match Add Successful")

        if (event_detail?.trim().toString()=="event"){

            val intent = Intent(this, MatchListActivity::class.java)
            intent.putExtra("EVENT_ID", event_id.toString())
            startActivity(intent)
            finish()
        }else {


            val intent = Intent(this, AddedMatchlistActivity::class.java)
            intent.putExtra("EVENT_ID", event_id.toString())
            startActivity(intent)
            finish()
        }
    }

    override fun onLocationListSuccess(response: List<LocationResult?>?) {

        hideLoader()

    }

    override fun onCoatListSuccess(response: List<Coat?>?) {

        try {

//            Log.d("coatList"," Response : "+GsonBuilder().setPrettyPrinting().create().toJson(response))
//            val coatList = ArrayList<String>()
//            for (i in response!!.indices){
//                coatList.add(response.get(i)!!.getId().toString());
//            }
//            Log.d("coatList",""+ coatList.size)



            hideLoader()
            val adapterCoat = ArrayAdapter(this, android.R.layout.simple_spinner_item, response!!)
            adapterCoat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCoat_add.adapter = adapterCoat


            spinnerCoat_add.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                ) {
                    var SelectedCoat = spinnerCoat_add.selectedItem as Coat
                    Log.d(
                            "TAG",
                            "onItemSelected: " + SelectedCoat.getId() + "  " + SelectedCoat.getCoatName()
                    )



                    txt_spinnerCoat_add.text = SelectedCoat.getId().toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }


            }
        }catch (e: java.lang.Exception){

        }


    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)

    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Home Screen")
        //set message for alert dialog
        builder.setMessage("Are you Sure you Want to Go Home Screen?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()

            super.onBackPressed()


        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked cancel\n operation cancel", Toast.LENGTH_LONG).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){ dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

}