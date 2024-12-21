package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityBookingDetail2Binding
import com.e.jannet_stable_code.databinding.ActivityEditMatchBinding
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.MatchListActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IAddMatchView
import com.e.jannet_stable_code.viewinterface.IEditMatchView
import com.e.jannet_stable_code.viewinterface.ILocationView
import com.e.jannet_stable_code.viewinterface.ITeamListView

import java.util.*
import kotlin.collections.ArrayList

class EditMatchActivity : BaseActivity(), ITeamListView, IEditMatchView, ILocationView {
    lateinit var teamListController: ITeamListController
    lateinit var addMatchController: IEditMatchController
    private lateinit var bind: ActivityEditMatchBinding

    private var teamListTemp: List<TeamListResult?>? = null
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.activity_edit_match)
        bind = ActivityEditMatchBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")
        val team_a_name = intent.getStringExtra("TEAM_A_NAME")
        val team_b_name = intent.getStringExtra("TEAM_B_NAME")
        val time = intent.getStringExtra("TIME")
        val coat = intent.getStringExtra("COAT")

        Log.e("EitMatch", "onCreate:team a name$team_a_name ")
        bind.txtSpinnervs1.setOnClickListener {

            bind.spinnervs1Edit.performClick()
        }

        bind.txtSpinnervs2.setOnClickListener {

            bind.spinnervs2Edit.performClick()
        }

        if (team_a_name != null) {

            bind.txtSpinnervs1.text = team_a_name
        }

        if (team_b_name != null) {

            bind.txtSpinnervs2.text = team_b_name
        }

        if (time != null) {

            bind.etxtMatchTimeEdit.text = time
        }

        if (coat != null) {

            bind.txtSpinnerCoat.text = coat
        }

        bind.txtSpinnerCoat.setOnClickListener {
            bind.spinnerCoatEdit.performClick()
        }

        Log.e("EditMatch", "event id ==$event_id ")
        addMatchController = EditMatchController(this, this)
        teamListController = TeamListController(this, this)
        teamListController.callTeamLostApi(id, token, event_id.toString())
        showLoader()

        bind.topBar.txtTitle.text = "EDIT Match"
        bind.topBar.imgBack.setOnClickListener {
            onBackPressed()
        }

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                bind.etxtMatchTimeEdit.setText(String.format("%d : %d", hourOfDay, minute))
            }
        }, hour, minute, false)


        bind.etxtMatchTimeEdit.setOnClickListener({ v ->
            mTimePicker.show()
        })


        bind.txtAddFmatchEventEdit.setOnClickListener {

            var selectedSpinnerAItem =  bind.spinnervs1Edit.selectedItem as TeamListResult
            var selectedSpinnerBItem =  bind.spinnervs2Edit.selectedItem as TeamListResult

            var teamAid = selectedSpinnerAItem.getTeamId().toString()
            var teamBid = selectedSpinnerBItem.getTeamId().toString()
            Log.e("TAG", "===A==$selectedSpinnerAItem======")
            Log.e("TAG", "===B==$selectedSpinnerAItem======")


            if (selectedSpinnerAItem == selectedSpinnerBItem) {


                showToast("Can't Create Match With Same Team!!")
            } else if ( bind.etxtMatchTimeEdit.text.toString() == "" &&  bind.etxtMatchTimeEdit.text.toString() == null) {

                showToast("Please Select Match Time")
            } else if (bind.txtSpinnerCoat.text.toString() == "" &&  bind.txtSpinnerCoat.text.toString() == null) {

                showToast("Please Select Coat ")

            } else if (bind.txtSpinnervs1.text.trim().toString() == bind.txtSpinnervs2.text.trim()
                    .toString()
            ) {

                showToast("Can't create match with same team")
            } else if (selectedSpinnerAItem.getTeamId() == null) {

                val team_a_id = intent.getStringExtra("TEAM_A_ID")

                teamAid = team_a_id.toString()

            } else if (selectedSpinnerBItem.getTeamId() == null) {

                val team_b_id = intent.getStringExtra("TEAM_B_ID")
                teamBid = team_b_id.toString()
            } else {

                //SharedPrefUserData(this).getSavedData().id
                val stordata = StoreUserData(this)
                val id = stordata.getString(Constants.COACH_ID)
                val token = stordata.getString(Constants.COACH_TOKEN)
                val matchid = intent.getStringExtra("MATCH_ID")

                showLoader()
                addMatchController.callEditMatchApi(
                    id, token, matchid.toString(), teamAid.toString(),
                    teamBid.toString(), bind.txtSpinnerCoat.text.toString(),
                    bind.etxtMatchTimeEdit.text.toString().filter { !it.isWhitespace() }
                )
            }

        }


        //spinner


//        val arrayAdapterTeamA =
//            ArrayAdapter(this, android.R.layout.simple_spinner_item, teamListTemp!!)
//        arrayAdapterTeamA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnervs1_edit.adapter = arrayAdapterTeamA
//
//        spinnervs1_edit.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//
//                var selectedTeamAItem = spinnervs1_edit.selectedItem as TeamListResult
//                Log.d(
//                    "TAG",
//                    "onItemSelected: " + selectedTeamAItem.getTeamId() + "  " + selectedTeamAItem.getTeamName()
//                )
//
//                txt_spinnervs1.text = selectedTeamAItem.getTeamName()
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//
//            }
//        }
//
//
//        val arrayAdapterTeamB =
//            ArrayAdapter(this, android.R.layout.simple_spinner_item, teamListTemp!!)
//        arrayAdapterTeamB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnervs2_edit.adapter = arrayAdapterTeamB
//
//        spinnervs2_edit.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//
//                var selectedTeamBItem = spinnervs2_edit.selectedItem as TeamListResult
//                Log.d(
//                    "TAG",
//                    "onItemSelected: " + selectedTeamBItem.getTeamId() + "  " + selectedTeamBItem.getTeamName()
//                )
//                txt_spinnervs2.text = selectedTeamBItem.getTeamName()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//
//            }
//        }
//


    }


    override fun onTeamListSuccess(teamList: List<TeamListResult?>?) {
        val team_a_name = intent.getStringExtra("TEAM_A_NAME")
        val team_b_name = intent.getStringExtra("TEAM_B_NAME")
        val team_a_id = intent.getStringExtra("TEAM_A_ID")
        val team_b_id = intent.getStringExtra("TEAM_B_ID")

        teamListTemp = teamList
        var tempTeamTypeList = ArrayList<TeamListResult?>()
        var listHint = TeamListResult()
        listHint.setTeamId(team_a_id?.toInt())
        listHint.setTeamName(team_a_name.toString())
        tempTeamTypeList.add(0, listHint)
        tempTeamTypeList.addAll(teamList!!)

        var tempTeamBTypeList = ArrayList<TeamListResult?>()
        var lisBtHint = TeamListResult()
        lisBtHint.setTeamId(team_b_id?.toInt())
        lisBtHint.setTeamName(team_b_name.toString())
        tempTeamBTypeList.add(0, lisBtHint)
        tempTeamBTypeList.addAll(teamList)

        val arrayAdapterTeamA =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempTeamTypeList)
        arrayAdapterTeamA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bind.spinnervs1Edit.adapter = arrayAdapterTeamA


//temp


        bind.spinnervs1Edit.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                var selectedTeamAItem = bind.spinnervs1Edit.selectedItem as TeamListResult
                Log.d(
                    "TAG",
                    "onItemSelected: " + selectedTeamAItem.getTeamId() + "  " + selectedTeamAItem.getTeamName()
                )



                bind.txtSpinnervs1.text = selectedTeamAItem.getTeamName()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        val arrayAdapterTeamB =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempTeamBTypeList)
        arrayAdapterTeamB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bind.spinnervs2Edit.adapter = arrayAdapterTeamB




        bind.spinnervs2Edit.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                var selectedTeamBItem =  bind.spinnervs2Edit.selectedItem as TeamListResult
                Log.d(
                    "TAG",
                    "onItemSelected: " + selectedTeamBItem.getTeamId() + "  " + selectedTeamBItem.getTeamName()
                )
                bind.txtSpinnervs2.text = selectedTeamBItem.getTeamName()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        hideLoader()
    }


    override fun onEditMatchSuccessful() {
        hideLoader()

        val event_id = intent.getStringExtra("EVENT_ID")
        val event_detail = intent.getStringExtra("EVENT_DETAIL")

        if (event_detail?.trim().toString() == "event_detail") {

        } else {

            val intent = Intent(this, MatchListActivity::class.java)
            intent.putExtra("EVENT_ID", event_id)
            startActivity(intent)
            finish()

        }

        val intent = Intent(this, AddedMatchlistActivity::class.java)
        intent.putExtra("EVENT_ID", event_id)
        startActivity(intent)
        finish()


    }

    override fun onLocationListSuccess(response: List<LocationResult?>?) {
        hideLoader()
    }

    override fun onCoatListSuccess(response: List<Coat?>?) {

        hideLoader()
        val adapterCoat =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, response!!)
        adapterCoat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bind.spinnerCoatEdit.adapter = adapterCoat


        bind.spinnerCoatEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var SelectedCoat = bind.spinnerCoatEdit.selectedItem as Coat
                Log.d(
                    "TAG",
                    "onItemSelected: " + SelectedCoat.getId() + "  " + SelectedCoat.getCoatName()
                )



                bind.txtSpinnerCoat.text = SelectedCoat.getId().toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }


    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)
    }
}