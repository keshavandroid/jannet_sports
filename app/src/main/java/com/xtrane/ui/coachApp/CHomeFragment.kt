package com.xtrane.ui.coachApp

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.xtrane.R
import com.xtrane.adapter.CoachFilterListAdapter
import com.xtrane.adapter.EventListAdapter
import com.xtrane.databinding.FragmentAllCoachesBinding
import com.xtrane.databinding.FragmentHomeCoachBinding
import com.xtrane.databinding.FragmentTeamBinding
import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.coachfilterdata.CoachFilterResult
import com.xtrane.retrofit.coachsportslistdata.CoachSportsListResult
import com.xtrane.retrofit.controller.*
import com.xtrane.retrofit.locationdata.Coat
import com.xtrane.retrofit.locationdata.LocationResult
import com.xtrane.ui.coachApp.addEventScreen.AddEventActivity
import com.xtrane.ui.parentsApp.EventDetailsActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.Constants.eventDetailTop
import com.xtrane.utils.CustomProgressDialog
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.*

class CHomeFragment : Fragment(), ICoachEventListView, ILocationView,
    ICoachSportsListVIew, ICoachFilterView, IAddMatchView {

    lateinit var controller: ICoachEventListController
    lateinit var coachomeFilterController: ICoachFilterController
    private var textLoadingDialog: CustomProgressDialog? = null
    lateinit var locationController: ILocationController
    private var loadingDialog: CustomProgressDialog? = null
    var locationResponse: List<LocationResult?>? = null
    var sportsListResponse: List<CoachSportsListResult?>? = null
    lateinit var coachSportsListCOntroller: ICoachSportsListController
    private lateinit var binding: FragmentHomeCoachBinding
    lateinit var deviceRegisterController: IDeviceRegisterController
    var newToken: String? = null

    private var id = "";
    private var token = "";
    private var email = "";

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val msg = intent?.getStringExtra("message")
            //  Toast.makeText(requireContext(), "FCM: $msg", Toast.LENGTH_SHORT).show()

            Log.d("BroadcastReceiver=", "receiver : " + msg)

            showCustomDialog(msg,"","")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeCoachBinding.inflate(layoutInflater)


        //   val view = super.onCreateView(inflater, container, savedInstanceState)

        val storedata = StoreUserData(requireContext())

        id = storedata.getString(Constants.COACH_ID)
        token = storedata.getString(Constants.COACH_TOKEN)
        email = storedata.getString(Constants.COACH_Email)

        val user_type = storedata.getString(Constants.COACH_TYPE)

        showLoader()

        controller = CoachEventListController(requireActivity(), this)
        controller.callCoachEventListApi(id, token, id, "all")
        deviceRegisterController = DeviceRegisterController(requireActivity(), this)

        Handler().postDelayed({
            callDeviceRegister(id!!, email!!)
        }, 5000)

        return binding.root

//        return view
    }

    private fun callDeviceRegister(id: String, email: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String? ->
            if (!TextUtils.isEmpty(token)) {
                /*Log.e("newToken", newToken);*/
                //newToken = token
            } else {
                /*Log.w(TAG, "token should not be null...");*/
            }
        }.addOnFailureListener { e: java.lang.Exception? ->

        }.addOnCanceledListener {

        }.addOnCompleteListener(fun(task: Task<String>) {
            Log.d("NEWTOKEN", "This is the token : " + task.result)
            newToken = task.result.toString()

            try {

                val deviceID = Settings.Secure.getString(
                    requireActivity().contentResolver,
                    Settings.Secure.ANDROID_ID
                )

                Log.d("FireToken", "TOKEN : " + newToken + "  deviceID :" + deviceID)

//                 jsonObject.put("regId", newToken)
//                 jsonObject.put("user_id", id)
//                 jsonObject.put("device_id", deviceID)
//                 jsonObject.put("email", email)

                deviceRegisterController.callDeviceRegister(newToken!!, id, deviceID, email)


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }


        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storedata = StoreUserData(requireContext())
        val id = storedata.getString(Constants.COACH_ID)
        val token = storedata.getString(Constants.COACH_TOKEN)
//        val user_type = storedata.getString(Constants.COACH_TYPE)

        setTopBar()

        coachomeFilterController = CoachFilterController(requireActivity(), this)

        val type = arguments?.getString("type")
        val message = arguments?.getString("message")
        val title = arguments?.getString("title")

        Log.e("CoachHometype=", type.toString() + "===")

        if (type != null && type.length > 0) {
            showCustomDialog(message, type, title!!)
        }

        binding.topbar.imgHistory.setOnClickListener {

            filterPopUp()

        }

        binding.imgPlus.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    AddEventActivity::class.java
                )
            )
        }
        binding.txtAllEvents.setOnClickListener {
            binding.txtMyEvents.setBackgroundResource(R.drawable.background_white_radius_4dp)
            binding.txtAllEvents.setBackgroundResource(R.drawable.square1)
            controller = CoachEventListController(requireActivity(), this)
            controller.callCoachEventListApi(id, token, id, "all")
        }
        binding.txtMyEvents.setOnClickListener {
            binding.txtMyEvents.setBackgroundResource(R.drawable.square1)
            binding.txtAllEvents.setBackgroundResource(R.drawable.background_white_radius_4dp)
            controller = CoachEventListController(requireActivity(), this)
            controller.callCoachEventListApi(id, token, id, "my")
        }

//        if (rcvEventList == null) {
//
//            showLoader()
////            controller = CoachEventListController(requireActivity(), this)
////            controller.callCoachEventListApi(id, token, id)
//
//        }
//        else {
//            showLoader()
////            controller = CoachEventListController(requireActivity(), this)
////

////            controller.callCoachEventListApi(id, token, id)
//            coachSportsListCOntroller = CoachSportsListController(requireActivity(), this)
//            locationController = LocationController(requireActivity(), this)
//            locationController.callLocationApi(id, token)
//            coachSportsListCOntroller.callCoachSportsListApi(id, token, id)
//
//
//        }


    }

    private fun filterPopUp() {

        var popupDialog = Dialog(requireContext())
        popupDialog.setCancelable(true)

        popupDialog.setContentView(R.layout.single_row_design_filter_poup_design)
        popupDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var location = popupDialog.findViewById<Spinner>(R.id.spinner_location)
        var sports = popupDialog.findViewById<Spinner>(R.id.spinner_sports)
        var apply = popupDialog.findViewById<TextView>(R.id.btn_apply)
        var clear = popupDialog.findViewById<TextView>(R.id.btn_clear)


        val arrayAdapterLocation =
            ArrayAdapter(requireContext(), R.layout.spinner_selected_item, locationResponse!!)
        arrayAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        location.adapter = arrayAdapterLocation

        location.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                var locationItem = location.selectedItem as LocationResult
                Log.e(
                    "TAG",
                    "onItemSelected: " + locationItem.getId() + "  " + locationItem.getName()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        //sports

        val arrayAdapterSports =
            ArrayAdapter(requireContext(), R.layout.spinner_selected_item, sportsListResponse!!)
        arrayAdapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sports.adapter = arrayAdapterSports

        sports.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                var SportsItem = sports.selectedItem as CoachSportsListResult
                Log.e(
                    "TAG",
                    "onItemSelected: " + SportsItem.getId() + "  " + SportsItem.getSportsName()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        apply.setOnClickListener {

            var locationItem = location.selectedItem as LocationResult
            var SportsItem = sports.selectedItem as CoachSportsListResult
            var storData = StoreUserData(requireContext())
            val id = storData.getString(Constants.COACH_ID)
            val token = storData.getString(Constants.COACH_TOKEN)

            Log.e("TAG", "filterPopUp: sportsId===${SportsItem.getId().toString()} ")
            coachomeFilterController.callCoachFilterApi(
                id,
                token,
                SportsItem.getId().toString(),
                locationItem.getId().toString()
            )
            showLoader()
//
//            Handler().postDelayed({
//
//                hideLoader()
//            }, 10000)
            popupDialog.dismiss()
        }

        clear.setOnClickListener {
            popupDialog.dismiss()
            controller.callCoachEventListApi(id, token, id, "all")
        }

        popupDialog.show()


    }

    override fun onResume() {
        super.onResume()

//        EventListController(requireActivity(), object : ControllerInterface {
//            override fun onFail(error: String?) {
//
//            }
//
//            override fun <T> onSuccess(response: T, method: String) {
//                try {
//                    val resp=response as EventListResponse
//
//                    setListAdapter(resp.getResult()!!.reversed())
//
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }).callApi()
    }

    private fun setListAdapter(result: List<CoachEventListResult?>?) {
        binding.rcvEventList.adapter = EventListAdapter(
            result, requireActivity(),
            object : EventListAdapter.AdapterListInterface {
                override fun onItemSelected(position: Int, data: CoachEventListResult) {
                    startActivity(
                        Intent(requireActivity(), EventDetailsActivity::class.java).putExtra(
                            "from",
                            "coachPersonal"
                        ).putExtra("eventId", data.getId().toString())


                    )
                    Log.e("CHome", "==event id====${data.getId().toString()}")

                }

            })
    }


    private fun setFilterListAdapter(result: List<CoachFilterResult?>?) {
        binding.rcvEventList.adapter = CoachFilterListAdapter(
            result, requireActivity(),
            object : CoachFilterListAdapter.AdapterListInterface {
                override fun onItemSelected(position: Int, data: CoachFilterResult) {
                    startActivity(
                        Intent(requireActivity(), EventDetailsActivity::class.java).putExtra(
                            "from",
                            "coachPersonal"
                        ).putExtra("eventId", data.getId().toString())


                    )

                    Log.e("CHome", "==event id====${data.getId().toString()}")

                }

            })
    }

    private fun setTopBar() {

        binding.topbar.imgBack.visibility = View.GONE
        binding.topbar.imgLogo.visibility = View.VISIBLE
        binding.topbar.imgHistory.visibility = View.VISIBLE
        binding.topbar.txtTitle.visibility = View.GONE
    }

    override fun onCoachEventListSuccess(response: List<CoachEventListResult?>?) {


        if (response != null) {
            hideLoader()
            setListAdapter(response!!.reversed())

        } else {
            hideLoader()
        }
        val storedata = StoreUserData(requireContext())
        val id = storedata.getString(Constants.COACH_ID)
        val token = storedata.getString(Constants.COACH_TOKEN)
        val user_type = storedata.getString(Constants.COACH_TYPE)

        showLoader()
        coachSportsListCOntroller = CoachSportsListController(requireActivity(), this)
        locationController = LocationController(requireActivity(), this)
        locationController.callLocationApi(id, token)
        coachSportsListCOntroller.callCoachSportsListApi(id, token, id)

    }

    override fun onLocationListSuccess(response: List<LocationResult?>?) {
        hideLoader()

        locationResponse = response!!


        Log.e("TAG", "onLocationListSuccess: location responde $locationResponse")
    }

    override fun onCoatListSuccess(response: List<Coat?>?) {
    }

//    override fun onSportListSuccess(sports: List<SportsListResponse.Result?>?) {
//
//        hideLoader()
//        sportsListResponse = sports!!
//    }

    override fun onGetSportsListSuccess(response: List<CoachSportsListResult?>?) {

        hideLoader()
        sportsListResponse = response!!


    }

    override fun onCoachFilterSuccess(response: List<CoachFilterResult?>?) {

        hideLoader()
        setFilterListAdapter(response)

    }

    override fun addMatchSuccessful() {
    }

    override fun showLoader() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) return
        hideLoader()
        loadingDialog = CustomProgressDialog(requireContext(), "")
        try {
            loadingDialog!!.show()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    override fun showLoader(message: String?) {

    }

    override fun hideLoader() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) try {
            loadingDialog!!.dismiss()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
        if (textLoadingDialog != null && textLoadingDialog!!.isShowing()) try {
            textLoadingDialog!!.dismiss()
        } catch (e: Exception) {
            // e.printStackTrace();
        }
    }

    override fun onFail(message: String?, e: Exception?) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

        hideLoader()

    }

    private fun showCustomDialog(msg: String?, type: String,title:String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_custom)

        val btnOk = dialog.findViewById<Button>(R.id.btnOk)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)

        tvTitle.text = title
        tvMessage.text = msg
        val eventId = arguments?.getString("eventId")

        btnOk.setOnClickListener {
            val sharedPref =
                requireActivity().getSharedPreferences("NotificationData", Context.MODE_PRIVATE)
            val notificationEditor = sharedPref.edit()
            notificationEditor.clear()
            notificationEditor.apply()


            if (type.equals("DraftStartingSoon")) {

                val intent = Intent(requireContext(), ParticipantsListActivity::class.java)
               //val intent = Intent(requireContext(), CoachListActivity::class.java)
                intent.putExtra("eventId",eventId)
                intent.putExtra("showbtn","yes")
                requireContext().startActivity(intent)
            }
            if (type.equals("CoachTurn")) {

                Log.e("Constants.eventDetailTop", eventDetailTop!!.getEventName()!!)
                val intent = Intent(requireContext(), ParticipantsListActivity::class.java)
                intent.putExtra("eventId",eventId)
                intent.putExtra("showbtn","yes")
                requireContext().startActivity(intent)
            }
//            else
//            {
//                dialog.dismiss()
//
//            }
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter("FCM_MESSAGE"))
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(receiver)
        super.onStop()
    }
}