package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MainTeamListAdapter
import com.e.jannet_stable_code.adapter.TeamListEDAdapter
import com.e.jannet_stable_code.retrofit.MainTeamListdata.MainTeamListResult
import com.e.jannet_stable_code.retrofit.controller.GetMainTeamListController
import com.e.jannet_stable_code.retrofit.controller.IMainTeamListController
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.TeamDetailActivity
import kotlinx.android.synthetic.main.fragment_team.*
import kotlinx.android.synthetic.main.topbar_layout.*
import com.e.jannet_stable_code.ui.parentsApp.TeamDetailsActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.CustomProgressDialog
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IGetMainTeamView
import kotlinx.android.synthetic.main.activity_teams.*

class CTeamFragment : Fragment(R.layout.fragment_team),IGetMainTeamView ,MainTeamListAdapter.IItemClickListner{

    lateinit var controller:IMainTeamListController
    private var textLoadingDialog: CustomProgressDialog? = null
    private var loadingDialog: CustomProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()

        val storedata = StoreUserData(requireContext())
        val id =  storedata.getString(Constants.COACH_ID)
        val token =storedata.getString(Constants.COACH_TOKEN)
        controller=GetMainTeamListController(requireActivity(),this)
        controller.callGetMainTeamLisApi(id,token)
        showLoader()
        imgPlus.setOnClickListener {

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
        txtTitle.text = resources.getString(R.string.team1)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener {
            (requireActivity() as CoachMainActivity).onBackPressed()
        }
    }

    override fun onMainTeamListSuccess(response: List<MainTeamListResult?>?) {

        hideLoader()

        var TeamAdapger = MainTeamListAdapter(requireContext(), response!!)
        rv_team_list_Main.adapter = TeamAdapger
        TeamAdapger.iItemClickListner=this
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
        Toast.makeText(context,"$message",Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(itemCLick: MainTeamListResult) {

        val intent = Intent(requireContext(), TeamDetailsActivity::class.java)
        intent.putExtra("TEAM_ID", itemCLick.getTeamId().toString())
        intent.putExtra("TEAM_NAME",itemCLick.getTeamName().toString())
        intent.putExtra("TEAM_DESCRIPTION",itemCLick.getDescription().toString())
        intent.putExtra("TEAM_Image",itemCLick.getImage().toString())

        startActivity(intent)
    }
}