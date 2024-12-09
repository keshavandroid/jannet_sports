package com.e.jannet_stable_code.ui.coachApp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.CoachFilterListAdapter
import com.e.jannet_stable_code.adapter.EventListAdapter
import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.coachfilterdata.CoachFilterResult
import com.e.jannet_stable_code.retrofit.coachsportslistdata.CoachSportsListResult
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.AddEventActivity
import kotlinx.android.synthetic.main.fragment_home_coach.*
import kotlinx.android.synthetic.main.topbar_layout.*
import com.e.jannet_stable_code.ui.parentsApp.EventDetailsActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.CustomProgressDialog
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.*

class CHomeFragment : Fragment(R.layout.fragment_home_coach), ICoachEventListView, ILocationView,
    ICoachSportsListVIew, ICoachFilterView {

    lateinit var controller: ICoachEventListController
    lateinit var coachomeFilterController: ICoachFilterController
    private var textLoadingDialog: CustomProgressDialog? = null
    lateinit var locationController: ILocationController
    private var loadingDialog: CustomProgressDialog? = null
     var locationResponse: List<LocationResult?>? = null
     var sportsListResponse: List<CoachSportsListResult?>? = null
     lateinit var coachSportsListCOntroller: ICoachSportsListController

    private var id = "";
    private var token = "";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val storedata = StoreUserData(requireContext())
        id = storedata.getString(Constants.COACH_ID)
        token = storedata.getString(Constants.COACH_TOKEN)
        val user_type = storedata.getString(Constants.COACH_TYPE)

        showLoader()
        controller = CoachEventListController(requireActivity(), this)
        controller.callCoachEventListApi(id, token, id)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storedata = StoreUserData(requireContext())
        val id = storedata.getString(Constants.COACH_ID)
        val token = storedata.getString(Constants.COACH_TOKEN)
        val user_type = storedata.getString(Constants.COACH_TYPE)
        coachomeFilterController = CoachFilterController(requireActivity(), this)
        setTopBar()

        imgHistory.setOnClickListener {

            filterPopUp()

        }

        imgPlus.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    AddEventActivity::class.java
                )
            )
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


        val arrayAdapterLocation = ArrayAdapter(requireContext(), R.layout.spinner_selected_item, locationResponse!!)
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

        val arrayAdapterSports = ArrayAdapter(requireContext(), R.layout.spinner_selected_item, sportsListResponse!!)
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

            Log.e("TAG", "filterPopUp: sportsId===${SportsItem.getId().toString()} ", )
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
            controller.callCoachEventListApi(id, token, id)
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
        rcvEventList.adapter = EventListAdapter(result, requireActivity(),
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
        rcvEventList.adapter = CoachFilterListAdapter(result, requireActivity(),
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
        imgBack.visibility = View.GONE
        imgLogo.visibility = View.VISIBLE
        imgHistory.visibility = View.VISIBLE
        txtTitle.visibility = View.GONE
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


}