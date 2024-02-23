package com.e.jannet_stable_code.ui.parentsApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MatchTabListAdapter
import com.e.jannet_stable_code.adapter.RegisterListAdapter
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.controller.GetParentMatchController
import com.e.jannet_stable_code.retrofit.controller.GetRegisterEventController
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult
import com.e.jannet_stable_code.retrofit.matchlistdata.RegisterListResponse
import com.e.jannet_stable_code.retrofit.matchlistdata.RegisterResult
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.Utilities
import kotlinx.android.synthetic.main.fragment_match.*
import kotlinx.android.synthetic.main.topbar_layout.*

class MatchFragment : Fragment(R.layout.fragment_match) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()
        setTab(1)
        setClickListener()
   }

        private fun openEventDetails() {
        startActivity(Intent(requireActivity(),EventDetailsActivity::class.java).putExtra("from","match"))
    }

    private fun setClickListener() {
        txtRegisterEvent.setOnClickListener {
            setTab(1)
        }
        txtChildBookEvent.setOnClickListener{
            setTab(2)
        }
        imgHistory.setOnClickListener{
            startActivity(
                Intent(activity, MatchHistoryActivity::class.java)
            )
        }
    }

    private fun setTab(i: Int) {
        if (i == 1) {
            txtRegisterEvent.setBackgroundResource(R.mipmap.button_small)
            txtChildBookEvent.setBackgroundResource(R.drawable.circle3)
            txtRegisterEvent.setTextColor(resources.getColor(R.color.white))
            txtChildBookEvent.setTextColor(resources.getColor(R.color.black))

            rcvRegisterEvent.visibility = View.VISIBLE
            rcvChildBookEvent.visibility = View.GONE
        } else if (i == 2) {
            txtRegisterEvent.setBackgroundResource(R.drawable.circle3)
            txtChildBookEvent.setBackgroundResource(R.mipmap.button_small)
            txtRegisterEvent.setTextColor(resources.getColor(R.color.black))
            txtChildBookEvent.setTextColor(resources.getColor(R.color.white))

            rcvRegisterEvent.visibility = View.GONE
            rcvChildBookEvent.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        //old API call in this screen
        /*GetParentMatchController(requireActivity(), object : ControllerInterface,
            MatchTabListAdapter.IMatchItemClickClickListner {
            override fun onFail(error: String?) {

            }
            override fun <T> onSuccess(response: T, method: String) {
                try {

                    val resp = response as  List<MatchListResult?>?
                    var adapter = MatchTabListAdapter(requireContext(), resp)
                    rcvMatchList.adapter = adapter
                    adapter.iMatchItemClickClickListner = this
                    adapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClick(result: MatchListResult) {
              //  var list:List<MatchListResult?>?=listOf<MatchListResult>()
                val intent = Intent (getActivity(), RegisteredMatchActivity::class.java)
                intent.putExtra("getTeamAName",result.getTeamAName())
                intent.putExtra("getTeamAImage",result.getTeamAImage())
                intent.putExtra("getTeamBName",result.getTeamBName())
                intent.putExtra("getTeamBImage",result.getTeamBImage())
                intent.putExtra("date",result.getDate()+result.getTime())
                getActivity()?.startActivity(intent)


            }
        }).callApi()*/





        val userType= SharedPrefUserData(requireActivity()).getSavedData().usertype
        if(userType.equals("child",ignoreCase = true) || userType.equals("adult",ignoreCase = true)){
            txtRegisterEvent.visibility = View.GONE
            txtChildBookEvent.visibility = View.GONE
            rcvRegisterEvent.visibility = View.GONE
            rcvChildBookEvent.visibility = View.GONE

            rcvMatchList.visibility = View.VISIBLE
            childAndAdultMatchList()
        }else if (userType.equals("parent",ignoreCase = true)){
            txtRegisterEvent.visibility = View.VISIBLE
            txtChildBookEvent.visibility = View.VISIBLE


            //new api call in  this screen
            setRegisterEventList()
        }

    }

    private fun childAndAdultMatchList(){
        GetParentMatchController(requireActivity(), object : ControllerInterface,
            MatchTabListAdapter.IMatchItemClickClickListner {
            override fun onFail(error: String?) {

            }
            override fun <T> onSuccess(response: T, method: String) {
                try {

                    val resp = response as  List<MatchListResult?>?
                    var adapter = MatchTabListAdapter(requireContext(), resp)
                    rcvMatchList.adapter = adapter
                    adapter.iMatchItemClickClickListner = this
                    adapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClick(result: MatchListResult) {
                //  var list:List<MatchListResult?>?=listOf<MatchListResult>()
                val intent = Intent (getActivity(), RegisteredMatchActivity::class.java)
                intent.putExtra("getTeamAName",result.getTeamAName())
                intent.putExtra("getTeamAImage",result.getTeamAImage())
                intent.putExtra("getTeamBName",result.getTeamBName())
                intent.putExtra("getTeamBImage",result.getTeamBImage())
                //intent.putExtra("date",result.getDate()+result.getTime())
                intent.putExtra("date", Utilities.convertDateFormat(result.getDate().toString())+result.getTime())
                getActivity()?.startActivity(intent)


            }
        }).callApi()
    }

    private fun setRegisterEventList(){
        GetRegisterEventController(requireActivity(), object : ControllerInterface,
            MatchTabListAdapter.IMatchItemClickClickListner {
            override fun onFail(error: String?) {

            }
            override fun <T> onSuccess(response: T, method: String) {
                try {

                    val resp = response as RegisterResult
                    var adapter = RegisterListAdapter(requireContext(), resp.getEventData(),"event")
                    rcvRegisterEvent.adapter = adapter

                    var adapter2 = RegisterListAdapter(requireContext(), resp.getTicketData(),"ticket")
                    rcvChildBookEvent.adapter = adapter2


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClick(result: MatchListResult) {
            }
        }).callApi()
    }


    private fun setTopBar() {
        imgBack.visibility=View.VISIBLE
        imgHistory.visibility=View.VISIBLE
        imgBack.setOnClickListener { (requireActivity() as ParentsMainActivity).onBackPressed() }
        txtTitle.text=getString(R.string.match1)
    }

}

