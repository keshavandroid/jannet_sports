package com.xtrane.ui.coachApp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.xtrane.R
import com.xtrane.adapter.MainTeamListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.FragmentTeamBinding
import com.xtrane.retrofit.MainTeamListdata.MainTeamListResult
import com.xtrane.retrofit.controller.GetMainTeamListController
import com.xtrane.retrofit.controller.IMainTeamListController
import com.xtrane.ui.parentsApp.TeamDetailsActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.CustomProgressDialog
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IGetMainTeamView


class CTeamFragment : Fragment(), IGetMainTeamView,
    MainTeamListAdapter.IItemClickListner {

    lateinit var controller: IMainTeamListController
    private var textLoadingDialog: CustomProgressDialog? = null
    private var loadingDialog: CustomProgressDialog? = null
    private lateinit var binding: FragmentTeamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTeamBinding.inflate(layoutInflater)
        return binding.root

      //  return inflater.inflate(R.layout.fragment_team, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()

        val storedata = StoreUserData(requireContext())
        val id = storedata.getString(Constants.COACH_ID)
        val token = storedata.getString(Constants.COACH_TOKEN)
        controller = GetMainTeamListController(requireActivity(), this)
        controller.callGetMainTeamLisApi(id, token)

        showLoader()

        binding.imgPlus.setOnClickListener {

            val intent = Intent(requireContext(), AddMainTeamActivity::class.java)
            startActivity(intent)

        }


//        ll1.setOnClickListener { startActivity(Intent(requireContext(), TeamDetailsActivity::class.java).putExtra("from","coachTeamDetail")) }
//        ll2.setOnClickListener { ll1.performClick() }
//        ll3.setOnClickListener { ll1.performClick() }
//        ll4.setOnClickListener { ll1.performClick() }
//        ll5.setOnClickListener { ll1.performClick() }
//        ll6.setOnClickListener { ll1.performClick() }
//        ll7.setOnClickListener { ll1.performClick() }
    }

    private fun setTopBar() {


        binding.topbar.txtTitle.text=resources.getString(R.string.team1)
        binding.topbar.imgBack.visibility=VISIBLE
        binding.topbar.imgBack.setOnClickListener {
            (requireActivity() as CoachMainActivity).onBackPressed()
        }
    }

    override fun onMainTeamListSuccess(response: List<MainTeamListResult?>?) {

        hideLoader()

        var TeamAdapger = MainTeamListAdapter(requireContext(), response!!)
        binding.rvTeamListMain.adapter = TeamAdapger
        TeamAdapger.iItemClickListner = this
        TeamAdapger.notifyDataSetChanged()

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
        TODO("Not yet implemented")
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
        Toast.makeText(context, "$message", Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(itemCLick: MainTeamListResult) {

        val intent = Intent(requireContext(), TeamDetailsActivity::class.java)
        intent.putExtra("TEAM_ID", itemCLick.getTeamId().toString())
        intent.putExtra("TEAM_NAME", itemCLick.getTeamName().toString())
        intent.putExtra("TEAM_DESCRIPTION", itemCLick.getDescription().toString())
        intent.putExtra("TEAM_Image", itemCLick.getImage().toString())

        startActivity(intent)
    }
}