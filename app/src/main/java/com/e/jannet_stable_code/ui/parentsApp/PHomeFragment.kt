package com.e.jannet_stable_code.ui.parentsApp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.CustomListLocationDialogAdapter
import com.e.jannet_stable_code.adapter.CustomListViewDialogAdapter
import com.e.jannet_stable_code.adapter.ParticipentListAdapter
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.coachsportslistdata.CoachSportsListResult
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.retrofit.response.EventListResponse
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.utils.*
import com.e.jannet_stable_code.viewinterface.IAddMatchView
import com.e.jannet_stable_code.viewinterface.IGetSportView
import com.e.jannet_stable_code.viewinterface.ILocationView
import com.google.android.gms.tasks.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

import org.json.JSONObject


class PHomeFragment : Fragment(R.layout.fragment_home_parent), ILocationView, IGetSportView,
    IAddMatchView {
    var sportsListTop = ArrayList<SportsListResponse.Result>()
    var sportsListStings = ArrayList<String>()
    private var mContext: Context? = null
    internal var customDialog: CustomListViewDialog? = null
    internal var customDialogSports: CustomListSportsDialog? = null
    lateinit var locationController: ILocationController
    lateinit var getSportsController: IGetSportController

    //Notification
    lateinit var deviceRegisterController: IDeviceRegisterController


    var strIDSports: String = ""
    var strIDLocation: String = ""
    var locationResponse: List<LocationResult?>? = null
    var sportsResponse: List<SportsListResponse.Result?>? = null
    var sportsListResponse: List<CoachSportsListResult?>? = null
    var sharedPreference: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    var newToken: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        FirebaseApp.initializeApp(requireContext())


        setTopBar()
        //val storedata = StoreUserData(requireContext())
        val id = SharedPrefUserData(requireActivity()).getSavedData().id
        val token = SharedPrefUserData(requireActivity()).getSavedData().token


        val email = SharedPrefUserData(requireActivity()).getSavedData().email

        locationController = LocationController(requireActivity(), this)
        getSportsController = GetSportsController(requireActivity(), this)
        getSportsController.callGetSportsApi(id, token)
        locationController.callLocationApi(id, token)
        sharedPreference =
            this.requireActivity().getSharedPreferences("PREFERENCE_NAME1", Context.MODE_PRIVATE)
        editor = sharedPreference!!.edit()


        deviceRegisterController = DeviceRegisterController(requireActivity(), this)


        //DEVICE REGISTER FOR PUSH NOTIFICATION
            Handler().postDelayed({
                callDeviceRegister(id, email)
            }, 5000)


    }

    private fun callDeviceRegister(id: String, email: String) {



        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String? ->
            if (!TextUtils.isEmpty(token)) {
                /*Log.e("newToken", newToken);*/
                //newToken = token
            } else {
                /*Log.w(TAG, "token should not be null...");*/
            }
        }.addOnFailureListener {
                e: java.lang.Exception? ->

        }.addOnCanceledListener {

        }.addOnCompleteListener(fun(task: Task<String>) {
            Log.d("NEWTOKEN", "This is the token : " + task.result)
            newToken = task.result.toString()

            try {


                val deviceID = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
                Log.d("FireToken", "TOKEN : " + newToken + "  deviceID :" + deviceID)

//                 jsonObject.put("regId", newToken)
//                 jsonObject.put("user_id", id)
//                 jsonObject.put("device_id", deviceID)
//                 jsonObject.put("email", email)

                deviceRegisterController.callDeviceRegister(newToken!!,id,deviceID,email)


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }


        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null

    }

    override fun onResume() {
        super.onResume()

        var strIDSports1 = sharedPreference!!.getString("strIDSportsHome", "")!!
        var strIDLocation1 = sharedPreference!!.getString("strIDLocation", "")!!
        if (strIDSports1!!.isEmpty()) {
            EventListController(requireActivity(), object : ControllerInterface {
                override fun onFail(error: String?) {

                }

                override fun <T> onSuccess(response: T, method: String) {
                    try {
                        val resp = response as EventListResponse
                        setListAdapter(resp.getResult()!!.reversed())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).callApi()
        } else {
            PFilterEventListController(
                    requireActivity(),
                    strIDSports1,
                    strIDLocation1,
                    object : ControllerInterface {
                        override fun onFail(error: String?) {

                        }

                        override fun <T> onSuccess(response: T, method: String) {
                            try {

                                val resp = response as EventListResponse
                                val respon: EventListResponse = EventListResponse()
                                if (resp.getResult() == null) {
                                    editor!!.clear()
                                    editor!!.commit()
                                    strIDLocation = "";
                                    strIDSports = "";
                                    for (i in 0..sportsResponse!!.size - 1) {
                                        if (sportsResponse!!.get(i)!!.isCheck.equals("1")) {
                                            sportsResponse!!.get(i)!!.isCheck = "0"
                                        }
                                    }
                                    for (i in 0..locationResponse!!.size - 1) {
                                        if (locationResponse!!.get(i)!!.getisCheck().equals("1")) {
                                            locationResponse!!.get(i)!!.setisCheck("0")
                                        }
                                    }
                                    rcvEventList.visibility = View.GONE
                                    var dialog =
                                            Dialog(requireActivity(), R.style.MyBottomSheetDialogTheme)
                                    val dialogview = LayoutInflater.from(context)
                                            .inflate(R.layout.dlg_confirm, null, false)
                                    val tv_title = dialogview.findViewById<TextView>(R.id.tv_title)
                                    val tv_ok = dialogview.findViewById<TextView>(R.id.tv_ok)
                                    tv_title.setText(R.string.NoEventFound)
                                    tv_ok.setOnClickListener(View.OnClickListener {
                                        dialog.dismiss()

                                        PFilterEventListController(
                                                requireActivity(),
                                                "",
                                                "",
                                                object : ControllerInterface {
                                                    override fun onFail(error: String?) {
                                                    }

                                                    override fun <T> onSuccess(
                                                            response: T,
                                                            method: String
                                                    ) {
                                                        try {
                                                            rcvEventList.visibility = View.VISIBLE
                                                            val resp = response as EventListResponse
                                                            setListAdapter(resp.getResult()!!.reversed())
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                    }
                                                }).callApi()
                                    })
                                    dialog?.setCancelable(true)
                                    dialog?.setContentView(dialogview)
                                    dialog?.show()

                                } else {

                                    setListAdapter(resp.getResult()!!.reversed())

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }).callApi()
        }

    }

    private fun setListAdapter(result: List<EventListResponse.Result?>) {
        rcvEventList.adapter = ParticipentListAdapter(
                result,
                requireActivity(),
                object : ParticipentListAdapter.AdapterListInterface {
                    override fun onItemSelected(position: Int, data: EventListResponse.Result?) {

                        startActivity(
                                Intent(
                                        requireActivity(),
                                        EventDetailsActivity::class.java
                                ).putExtra("from", "home")
                                        .putExtra("eventId", data?.getId().toString())
                                        .putExtra("fees", data!!.getFees())
                                        .putExtra("parent", "parent")
                        )
                        Log.e("CHome", "==event id====${data?.getId().toString()}")

                    }

                })
    }

    private fun setTopBar() {
        imgBack.visibility = View.GONE
        imgLogo.visibility = View.VISIBLE
        imgFilter.visibility = View.VISIBLE
        txtTitle.visibility = View.GONE
        imgFilter.setOnClickListener(View.OnClickListener { showBottomSheetDialog() })
    }

    private fun showBottomSheetDialog() {

        val bottomSheetDialog =
            BottomSheetDialog(this.requireContext(), R.style.MyBottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.home_filter_bottom_sheet_dialog)

        val tv_sport = bottomSheetDialog.findViewById<TextView>(R.id.tv_sport)
        val tvLocation = bottomSheetDialog.findViewById<TextView>(R.id.tv_Location)
        val txtApply = bottomSheetDialog.findViewById<TextView>(R.id.txtApply)
        val txtClear = bottomSheetDialog.findViewById<TextView>(R.id.txtClear)


        var idSports = sharedPreference!!.getString("strIDSportsHome", "")
        if (idSports!!.isEmpty()) {
            strIDSports = ""

        } else {
            var strSports: String = ""
            val list: List<String> = listOf(*idSports.split(",").toTypedArray())
            for (i in 0..sportsResponse!!.size - 1) {
                for (j in 0..list!!.size - 1) {
                    if (sportsResponse!!.get(i)!!.id!!.toString().equals(list.get(j))) {
                        sportsResponse!!.get(i)!!.isCheck = "1"
                        if (!strSports.isEmpty()) {
                            strSports = strSports + "," + sportsResponse!!.get(i)!!.sportsName
                            strIDSports = strIDSports + "," + sportsResponse!!.get(i)!!.id

                        } else {
                            strSports += sportsResponse!!.get(i)!!.sportsName
                            strIDSports += sportsResponse!!.get(i)!!.id
                        }

                    }
                }

            }

            tv_sport!!.setText(strSports)
        }
        var strIDLoc = sharedPreference!!.getString("strIDLocation", "")
        if (strIDLoc!!.isEmpty()) {
            strIDSports = ""

        } else {
            var strLoc: String = ""
            val list: List<String> = listOf(*strIDLoc.split(",").toTypedArray())
            for (i in 0..locationResponse!!.size - 1) {
                for (j in 0..list!!.size - 1) {
                    if (locationResponse!!.get(i)!!.getId()!!.toString().equals(list.get(j))) {
                        locationResponse!!.get(i)!!.setisCheck("1")
                        if (!strLoc.isEmpty()) {
                            strLoc = strLoc + "," + locationResponse!!.get(i)!!.getName()
                            strIDLocation =
                                strIDLocation + "," + locationResponse!!.get(i)!!.getId()

                        } else {
                            strLoc += locationResponse!!.get(i)!!.getName()
                            strIDLocation += locationResponse!!.get(i)!!.getId()
                        }

                    }
                }

            }

            tvLocation!!.setText(strLoc)
        }

        tv_sport?.setOnClickListener(View.OnClickListener {

            val adapter = CustomListViewDialogAdapter(
                    sportsResponse,
                    object : CustomListViewDialogAdapter.AdapterListInterface {
                        override fun onItemSelected(position: Int, data: String) {
                            Log.d("sports..:", data)
                            //customDialogSports!!.dismiss()
                        }
                    })
            customDialogSports = CustomListSportsDialog(this.requireActivity(), adapter)

            customDialogSports!!.setDoneInterface(object : CustomListSportsDialog.DoneInterface {
                override fun onItemSelected() {
                    customDialogSports!!.dismiss()
                    var strSports: String = ""
                    strIDSports = ""
                    for (i in 0..sportsResponse!!.size - 1) {
                        if (sportsResponse!!.get(i)!!.isCheck.equals("1")) {
                            if (!strSports.isEmpty()) {

                                strSports = strSports + "," + sportsResponse!!.get(i)!!.sportsName
                                strIDSports = strIDSports + "," + sportsResponse!!.get(i)!!.id
                            } else {
                                strSports += sportsResponse!!.get(i)!!.sportsName
                                strIDSports += sportsResponse!!.get(i)!!.id
                            }

                        }
                    }

                    tv_sport.setText(strSports)
                }

                override fun onSelectAll() {
                    adapter.selectAllItems(true)
                }

            })
            customDialogSports!!.show()
            customDialogSports!!.setCanceledOnTouchOutside(false)

        })

        tvLocation?.setOnClickListener(View.OnClickListener {

            val adapter = CustomListLocationDialogAdapter(
                    locationResponse,
                    object : CustomListLocationDialogAdapter.AdapterListInterface {
                        override fun onItemSelected(position: Int, data: String) {
                            Log.d("Location..:", data)
                            //customDialog!!.dismiss();
                        }
                    })
            customDialog = CustomListViewDialog(this.requireActivity(), adapter)
            customDialog!!.setDoneInterface(object : CustomListViewDialog.DoneInterface {
                override fun onItemSelected() {
                    strIDLocation = ""
                    customDialog!!.dismiss()
                    var str: String = ""

                    for (i in 0..locationResponse!!.size - 1) {
                        if (locationResponse!!.get(i)!!.getisCheck().equals("1")) {
                            if (!str.isEmpty()) {

                                str = str + "," + locationResponse!!.get(i)!!.getName()
                                strIDLocation =
                                        strIDLocation + "," + locationResponse!!.get(i)!!.getId()
                            } else {
                                str += locationResponse!!.get(i)!!.getName()
                                strIDLocation += locationResponse!!.get(i)!!.getId()
                            }
                        }
                    }

                    tvLocation.setText(str)

                }
            })
            customDialog!!.show()
            customDialog!!.setCanceledOnTouchOutside(false)

            //(this.requireActivity() as CustomListViewDialogAdapter).methodNm()

        })

        txtApply?.setOnClickListener(View.OnClickListener {
            PFilterEventListController(
                    requireActivity(),
                    strIDSports,
                    strIDLocation,
                    object : ControllerInterface {
                        override fun onFail(error: String?) {

                        }

                        override fun <T> onSuccess(response: T, method: String) {
                            try {

                                val resp = response as EventListResponse
                                val respon: EventListResponse = EventListResponse()
                                if (resp.getResult() == null) {

                                    rcvEventList.visibility = View.GONE
                                    var dialog =
                                            Dialog(requireActivity(), R.style.MyBottomSheetDialogTheme)
                                    val dialogview = LayoutInflater.from(context)
                                            .inflate(R.layout.dlg_confirm, null, false)
                                    val tv_title = dialogview.findViewById<TextView>(R.id.tv_title)
                                    val tv_ok = dialogview.findViewById<TextView>(R.id.tv_ok)
                                    tv_title.setText(R.string.NoEventFound)
                                    tv_ok.setOnClickListener(View.OnClickListener {
                                        dialog.dismiss()
                                        editor!!.clear()
                                        editor!!.commit()
                                        strIDLocation = "";
                                        strIDSports = "";
                                        for (i in 0..sportsResponse!!.size - 1) {
                                            if (sportsResponse!!.get(i)!!.isCheck.equals("1")) {
                                                sportsResponse!!.get(i)!!.isCheck = "0"
                                            }
                                        }
                                        for (i in 0..locationResponse!!.size - 1) {
                                            if (locationResponse!!.get(i)!!.getisCheck().equals("1")) {
                                                locationResponse!!.get(i)!!.setisCheck("0")
                                            }
                                        }
                                        PFilterEventListController(
                                                requireActivity(),
                                                "",
                                                "",
                                                object : ControllerInterface {
                                                    override fun onFail(error: String?) {
                                                    }

                                                    override fun <T> onSuccess(
                                                            response: T,
                                                            method: String
                                                    ) {
                                                        try {
                                                            rcvEventList.visibility = View.VISIBLE
                                                            val resp = response as EventListResponse
                                                            setListAdapter(resp.getResult()!!.reversed())
                                                            bottomSheetDialog.dismiss()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                    }
                                                }).callApi()
                                    })
                                    dialog?.setCancelable(true)
                                    dialog?.setContentView(dialogview)
                                    dialog?.show()


                                } else {
                                    editor!!.putString("strIDSportsHome", strIDSports)
                                    editor!!.putString("strIDLocation", strIDLocation)
                                    editor!!.commit()
                                    setListAdapter(resp.getResult()!!.reversed())

                                }
                                bottomSheetDialog.dismiss()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }).callApi()
        })

        txtClear!!.setOnClickListener(View.OnClickListener {
            editor!!.clear()
            editor!!.commit()
            strIDLocation = "";
            strIDSports = "";
            for (i in 0..sportsResponse!!.size - 1) {
                if (sportsResponse!!.get(i)!!.isCheck.equals("1")) {
                    sportsResponse!!.get(i)!!.isCheck = "0"
                }
            }
            for (i in 0..locationResponse!!.size - 1) {
                if (locationResponse!!.get(i)!!.getisCheck().equals("1")) {
                    locationResponse!!.get(i)!!.setisCheck("0")
                }
            }

            PFilterEventListController(
                    requireActivity(),
                    "",
                    "",
                    object : ControllerInterface {
                        override fun onFail(error: String?) {
                        }

                        override fun <T> onSuccess(response: T, method: String) {
                            try {
                                val resp = response as EventListResponse
                                setListAdapter(resp.getResult()!!.reversed())
                                bottomSheetDialog.dismiss()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }).callApi()
        })
        bottomSheetDialog.show()
    }


    interface DoneInterFace {
        fun done(str: String)
    }

    private fun getSportsList() {
        GetSportsListController(this.requireActivity(), object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as SportsListResponse
                    /* sportsList = ArrayList(data.getResult()!!)

                     for (i in sportsList.indices) {
                         sportsStrList.add(sportsList[i]!!.sportsName!!)
                     }*/
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }).callApi()
    }

    /*  private fun getSportsList() {
          GetSportsListController(this.requireActivity(), object : ControllerInterface {
              override fun onFail(error: String?) {

                  showToast(requireActivity(),error.toString())
                  Log.e("sports", "onFail:$error ", )

              }

              override fun <T> onSuccess(response: T, method: String) {
                  try {
                      val data = response as SportsListResponse
                      setSportsList(data.getResult()!!)
                  } catch (e: Exception) {
                      e.printStackTrace()
                  }

              }

          }).callApi()
      }*/
    fun setSportsList(sportsList: List<SportsListResponse.Result?>) {
        for (i in sportsList.indices) {
            try {
                sportsListTop.add(sportsList[i]!!)
                sportsListStings.add(sportsList[i]!!.sportsName!!)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onLocationListSuccess(response: List<LocationResult?>?) {
        locationResponse = response!!
        Log.e("TAG1", "locationResponse $locationResponse")
    }

    override fun onCoatListSuccess(response: List<Coat?>?) {

    }

    override fun onSportListSuccess(sports: List<SportsListResponse.Result?>?) {
        this.sportsResponse = sports
        Log.e("TAG1", "sportsResponse $sportsResponse")
    }

    override fun addMatchSuccessful() {
        TODO("Not yet implemented")
    }

    override fun showLoader() {

    }

    override fun showLoader(message: String?) {

    }

    override fun hideLoader() {

    }


    override fun onFail(message: String?, e: Exception?) {

    }


}