package com.xtrane.ui.parentsApp

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.xtrane.R
import com.xtrane.adapter.*
import com.xtrane.databinding.FragmentAllCoachesBinding
import com.xtrane.databinding.FragmentTeamBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.alreadyjointeam.AlreadyJoinTeamResult
import com.xtrane.retrofit.coachsportslistdata.CoachSportsListResult
import com.xtrane.retrofit.coachteamdata.CoachTeamResult
import com.xtrane.retrofit.controller.*
import com.xtrane.retrofit.locationdata.Coat
import com.xtrane.retrofit.locationdata.LocationResult
import com.xtrane.retrofit.parentbootomcoach.CoachListResult
import com.xtrane.retrofit.response.EventListResponse
import com.xtrane.retrofit.response.SportsListResponse
import com.xtrane.utils.*
import com.xtrane.viewinterface.*
import com.google.android.material.bottomsheet.BottomSheetDialog



class AllCoachesFragment : Fragment(), IParentBootomCoachesView,
    ParentBottomCoachesAdapter.ICoachClickListner, ICoachTeamListView, IGetSportView,
    CoachTeamListAdapter.ITeamClickListner, ICoachAlreadyJoinTeamView,
    CoachAlreadyJoinTeamAdapter.ITeamClickListner, ILocationView {


    private var textLoadingDialog: CustomProgressDialog? = null
    private var loadingDialog: CustomProgressDialog? = null
    lateinit var coachController: IParentCoachesListController
    lateinit var coachControllerFilter: IPGetCoachListSportsTypeController
    lateinit var coachRecyclerView: RecyclerView
    lateinit var coachTeamController: ICoachTeamListController
    lateinit var coachJoinTeamController: ICoachAlreadyJoinTeamController
    lateinit var locationController: ILocationController
    var strIDSports: String = ""
    var strIDLocation: String = ""
    var sportsResponse: List<SportsListResponse.Result?>? = null
    var locationResponse: List<LocationResult?>? = null
    var sportsListResponse: List<CoachSportsListResult?>? = null
    internal var customDialogSports: CustomListSportsDialog? = null
    internal var customDialog: CustomListViewDialog? = null
    lateinit var getSportsController: IGetSportController
    var id: String = "";
    var token: String = "";

    var sharedPreference:SharedPreferences?=null
    var editor:SharedPreferences.Editor? = null
    private lateinit var binding: FragmentAllCoachesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCoachesBinding.inflate(layoutInflater)
        return binding.root

      //  return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coachTeamController = CoachTeamListController(requireActivity(), this)
        locationController = LocationController(requireActivity(), this)
        sharedPreference= this.requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        editor=sharedPreference!!.edit()

        setTopBar()

        coachRecyclerView = view.findViewById(R.id.rv_coach_list_parent)
        coachJoinTeamController = CoachAlreadyJoinTeamController(requireActivity(), this)


        val storeData = StoreUserData(requireContext())
        id = SharedPrefUserData(requireActivity()).getSavedData().id!!
        token = SharedPrefUserData(requireActivity()).getSavedData().token!!
        var idSports = sharedPreference!!.getString("strIDSports", "")
        if (idSports!!.isEmpty()) {
            coachController = ParentCoachesListController(requireActivity(), this)
            coachController.callCOachListApi(id, token)
        } else {
            coachControllerFilter = PGetCoachListSportsTypeController(requireActivity(), this)
            coachControllerFilter.callCOachListApi(id, token, idSports)
        }

        getSportsController = GetSportsController(requireActivity(), this)
        getSportsController.callGetSportsApi(id, token)
        locationController.callLocationApi(id,token)


        showLoader()

        binding.txtAllCoaches.setOnClickListener {
            setTab(1)
        }

        binding.txtChild.setOnClickListener {
            setTab(2)
//            (coachTeamController as CoachTeamListController).callCoachTeamListApi(id,token)

            val id = SharedPrefUserData(requireActivity()).getSavedData().id
            val token = SharedPrefUserData(requireActivity()).getSavedData().token
            coachJoinTeamController.callCoachJoinTeamApi(id!!, token!!)
            showLoader()

        }
        setTab(1)

        binding.topbar.imgLoc.visibility = View.VISIBLE

    }

    private fun openTeamDetailScreen() {
        startActivity(
            Intent(requireActivity(), TeamDetailsActivity::class.java).putExtra(
                "from",
                "allcoaches"
            )
        )
    }

    private fun setTab(i: Int) {
        if (i == 1) {
            binding.txtAllCoaches.setBackgroundResource(R.mipmap.button_small)
            binding.txtChild.setBackgroundResource(R.drawable.circle3)
            binding.txtAllCoaches.setTextColor(resources.getColor(R.color.white))
            binding.txtChild.setTextColor(resources.getColor(R.color.black))
            binding.llAllCoaches.visibility = View.VISIBLE
            binding.llJoinTeam.visibility = View.GONE
            binding.topbar.imgFilter.visibility = View.VISIBLE

            //AKSHAY VISIBLE this to show location button in title bar on top
            binding.topbar.imgLoc.visibility = View.GONE

            binding.topbar.imgFilter.setOnClickListener(View.OnClickListener { showBottomSheetDialog() })
            binding.topbar.imgLoc.setOnClickListener { showLocationBottomSheetDialog() }
        } else if (i == 2) {
            binding.txtAllCoaches.setBackgroundResource(R.drawable.circle3)
            binding.txtChild.setBackgroundResource(R.mipmap.button_small)
            binding.txtAllCoaches.setTextColor(resources.getColor(R.color.black))
            binding.txtChild.setTextColor(resources.getColor(R.color.white))
            binding.llAllCoaches.visibility = View.GONE
            binding.llJoinTeam.visibility = View.VISIBLE
            binding.topbar.imgLoc.visibility = View.GONE
            binding.topbar.imgFilter.visibility = View.GONE
        }
    }

    private fun showBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this.requireContext(), R.style.MyBottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.home_filter_bottom_sheet_dialog)

        val tv_sport = bottomSheetDialog.findViewById<TextView>(R.id.tv_sport)
        val tv_title = bottomSheetDialog.findViewById<TextView>(R.id.tv_title)
        val tvLocation = bottomSheetDialog.findViewById<TextView>(R.id.tv_Location)
        val txtApply = bottomSheetDialog.findViewById<TextView>(R.id.txtApply)
        val txtClear = bottomSheetDialog.findViewById<TextView>(R.id.txtClear)
        val ll_location = bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_location)
        ll_location!!.visibility = View.GONE
        tv_title!!.setText(resources.getText(R.string.Please_Select_sports))
        strIDSports = "";
        var idSports = sharedPreference!!.getString("strIDSports", "")
        if (idSports!!.isEmpty()) {
            strIDSports=""

        } else {
            var strSports: String = ""
            val list: List<String> = listOf(*idSports.split(",").toTypedArray())
            for (i in 0..sportsResponse!!.size - 1) {
                for(j in 0..list!!.size - 1){
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


        txtApply?.setOnClickListener(View.OnClickListener {

            try {
                coachControllerFilter = PGetCoachListSportsTypeController(requireActivity(), this)
                coachControllerFilter.callCOachListApi(id, token, strIDSports)
                bottomSheetDialog.dismiss()
                editor!!.putString("strIDSports", strIDSports)
                editor!!.commit()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
        txtClear!!.setOnClickListener(View.OnClickListener {

            try {
                bottomSheetDialog.dismiss()
                editor!!.clear()
                editor!!.commit()
                coachController = ParentCoachesListController(requireActivity(), this)
                coachController.callCOachListApi(id, token)
                for (i in 0..sportsResponse!!.size - 1) {
                    if (sportsResponse!!.get(i)!!.isCheck.equals("1")) {
                        sportsResponse!!.get(i)!!.isCheck = "0"
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
        bottomSheetDialog.show()
    }

    private fun showLocationBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this.requireContext(), R.style.MyBottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.home_filter_bottom_sheet_dialog)

        val tv_sport = bottomSheetDialog.findViewById<TextView>(R.id.tv_sport)
        val tv_title = bottomSheetDialog.findViewById<TextView>(R.id.tv_title)
        val tvLocation = bottomSheetDialog.findViewById<TextView>(R.id.tv_Location)
        val txtApply = bottomSheetDialog.findViewById<TextView>(R.id.txtApply)
        val txtClear = bottomSheetDialog.findViewById<TextView>(R.id.txtClear)
        val linSports = bottomSheetDialog.findViewById<LinearLayout>(R.id.linSports)
        linSports!!.visibility = View.GONE
        tv_title!!.setText(resources.getText(R.string.please_select_location))
        strIDSports = "";
        var strIDLoc = sharedPreference!!.getString("strIDLocation", "")
        if (strIDLoc!!.isEmpty()) {
            strIDSports = ""

        } else {
            var strLoc: String = ""
            val list: List<String> = listOf(*strIDLoc.split(",").toTypedArray())
            for (i in 0..sportsResponse!!.size - 1) {
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
            tvLocation!!.setOnClickListener(View.OnClickListener {

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

                        tvLocation!!.setText(str)

                    }
                })
                customDialog!!.show()
                customDialog!!.setCanceledOnTouchOutside(false)

                //(this.requireActivity() as CustomListViewDialogAdapter).methodNm()

            })

        txtApply?.setOnClickListener(View.OnClickListener {

        })

        txtClear!!.setOnClickListener(View.OnClickListener {
            try {
                bottomSheetDialog.dismiss()
                editor!!.clear()
                editor!!.commit()
                coachController = ParentCoachesListController(requireActivity(), this)
                coachController.callCOachListApi(id, token)
                for (i in 0..sportsResponse!!.size - 1) {
                    if (sportsResponse!!.get(i)!!.isCheck.equals("1")) {
                        sportsResponse!!.get(i)!!.isCheck = "0"
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
        bottomSheetDialog.show()
    }

    private fun openCoachDetailScreen() {
        startActivity(Intent(requireActivity(), CoachesDetailActivity::class.java))
    }

    private fun setTopBar() {
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener { (requireActivity() as ParentsMainActivity).onBackPressed() }
        binding.topbar.txtTitle.text = getString(R.string.all_coaches)
    }

    override fun onBottomCoachesListSuccess(response: List<CoachListResult?>?) {

        hideLoader()
        val parentCoachAdapter = ParentBottomCoachesAdapter(requireContext(), response)
        binding.rvCoachListParent.adapter = parentCoachAdapter
        parentCoachAdapter.iCoachClickListner = this
        parentCoachAdapter.notifyDataSetChanged()

    }

    override fun onCoachTeamListSuccess(response: List<CoachTeamResult?>?) {

        hideLoader()
    }

    override fun alredyJoinTeamSuccess(resonse: List<AlreadyJoinTeamResult?>?) {

        val parentCoachAdapter = CoachAlreadyJoinTeamAdapter(requireContext(), resonse!!)
        binding.rvAllCoachJoinTeam.adapter = parentCoachAdapter
        parentCoachAdapter.iTeamClickListner = this
        parentCoachAdapter.notifyDataSetChanged()
        hideLoader()


    }

    override fun onSportListSuccess(sports: List<SportsListResponse.Result?>?) {
        this.sportsResponse = sports
        Log.e("TAG1", "sportsResponse $sportsResponse")
    }

    override fun onLocationListSuccess(response: List<LocationResult?>?) {
        locationResponse = response!!
    }

    override fun onCoatListSuccess(response: List<Coat?>?) {

    }

    override fun showLoader() {

        if (loadingDialog != null && loadingDialog!!.isShowing()) return
        hideLoader()
        loadingDialog = CustomProgressDialog(requireContext(), "")
        try {
            loadingDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace();
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

        hideLoader()
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onCoachClick(response: CoachListResult?) {

        val id = response?.getId()
        val email = response?.getEmail()
        val birthday = response?.getBirthdate()
        val contact = response?.getContactNo()
        val gender = response?.getGender()
        val name = response?.getName()
        val image = response?.getImage()
        val intent = Intent(requireActivity(), CoachesDetailActivity::class.java)
        intent.putExtra("CoachID", id.toString())
        intent.putExtra("EMAIL", email.toString())
        intent.putExtra("BIRTH", birthday.toString())
        intent.putExtra("CONTACT", contact.toString())
        intent.putExtra("GENDER", gender.toString())
        intent.putExtra("NAME", name.toString())
        intent.putExtra("IMAGE", image.toString())
        intent.putExtra("BOTTOM_COACH", "bottom_coach")


        startActivity(intent)
    }

    override fun onTeamClick(response: CoachTeamResult?) {

    }

    override fun onTeamClick(response: AlreadyJoinTeamResult?) {

    }


}