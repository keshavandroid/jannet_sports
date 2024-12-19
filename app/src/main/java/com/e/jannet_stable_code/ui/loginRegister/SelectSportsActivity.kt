package com.e.jannet_stable_code.ui.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.ui.coachApp.CoachMainActivity
import com.e.jannet_stable_code.adapter.SportsGridListAdapter
import com.e.jannet_stable_code.databinding.ActivityMatchListBinding
import com.e.jannet_stable_code.databinding.ActivitySelectSportsBinding
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.coachsportslistdata.CoachSportsListResult
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.ParentsMainActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities
import com.e.jannet_stable_code.viewinterface.ICoachSportsListVIew
import com.google.gson.GsonBuilder
import kotlin.Exception

class SelectSportsActivity : BaseActivity(), ICoachSportsListVIew {
    var adapter:SportsGridListAdapter?=null
    lateinit var coachSportsListCOntroller: ICoachSportsListController
    var selectedSportsId= ArrayList<CoachSportsListResult>()
        var data:List<SportsListResponse.Result?>?=null

    override fun getController(): IBaseController? {
        return null
    }
    private lateinit var binding: ActivitySelectSportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///setContentView(R.layout.activity_select_sports)
        binding = ActivitySelectSportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coachSportsListCOntroller = CoachSportsListController(this, this)

        setTopBar()

        binding.txtFinish.setOnClickListener {

            if(adapter!=null) {
                val list=adapter!!.getList()

                var string=""

                for (i in list.indices){
                    if(string=="") {
                        if(list[i]!!.selected){
                            string= list[i]!!.id.toString()
                        }
                    }else{
                        if(list[i]!!.selected){
                            string=string+","+ list[i]!!.id.toString()
                        }
                    }
                }

                if(string=="") Utilities.showToast(this,"Select at least one sport to continue.")
                else{
                    SaveSportListController(this, string, object : ControllerInterface {
                        override fun onFail(error: String?) {

                        }

                        override fun <T> onSuccess(response: T, method: String) {

                            val coach = intent.getStringExtra("COACH").toString()

                            if (SharedPrefUserData(this@SelectSportsActivity).getSavedData().usertype == "adult" ||
                                SharedPrefUserData(this@SelectSportsActivity).getSavedData().usertype == "parent"
                            ) {

                                startActivity(
                                    Intent(
                                        this@SelectSportsActivity,
                                        ParentsMainActivity::class.java).putExtra("from","register")
                                )

                            }else if (coach.trim().toString()=="coach") {

                                onBackPressed()
                                finish()
                            }else{

                                startActivity(
                                    Intent(
                                        this@SelectSportsActivity,
                                        CoachMainActivity::class.java
                                    ).putExtra("from","register")
                                )
                            }
//
//                            startActivity(
//                                    Intent(
//                                        this@SelectSportsActivity,
//                                        CoachMainActivity::class.java
//                                    ).putExtra("from","register")
//                                )
//                            finish()
                        }
                    })
                }
            }
            /**/
        }

        GetSportsListController(context = this, object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {

                Log.d("sportsGet","111");

                val storeData = StoreUserData(this@SelectSportsActivity)
                val id = storeData.getString(Constants.COACH_ID)
                val token = storeData.getString(Constants.COACH_TOKEN)
                coachSportsListCOntroller.callCoachSportsListApi(id, token, id)

                Log.d("sportsGet"," Data : " + id);


                try {
                    val resp = response as SportsListResponse
//                    adapter=SportsGridListAdapter(resp.getResult()!!, this@SelectSportsActivity)
//                    rcvSportsGridList.adapter =adapter

                     data = resp.getResult()



                    Log.d("sportsGet"," Data : " + GsonBuilder().setPrettyPrinting().create().toJson(data));

                    adapter=SportsGridListAdapter(data!!, this@SelectSportsActivity)
                    binding.rcvSportsGridList.adapter = adapter


//                    try {
//
//
//                        data?.forEach { sports ->
//                            val id = sports?.id
//                            Log.e("TAG", "onSuccess: alll  id $id")
//
//                            selectedSportsId.forEach {
//                                val selectedId = it.getId()
//
//                                Log.e("TAG", "onSuccess: selected id is sssss$selectedId" )
//                                if (id?.toInt() == selectedId?.toInt()) {
//                                    sports?.selectedCoach = true
//                                    Log.e("TAG", "onSuccess: same id $id========selected id===$selectedId")
//                                } else {
//
//                                    sports?.selectedCoach = false
//
//                                }
//                            }
//
//
//                        }
//
//
//
//                    }
//                    catch (e:Exception){
//
//                        e.printStackTrace()
//                        Log.e("TAG", "onSuccess: inside exception$e", )
//                    }
                } catch (e: Exception) {
                    Log.d("sportsGet","333");


                    e.printStackTrace()
                    binding.rcvSportsGridList.adapter = SportsGridListAdapter(ArrayList(), this@SelectSportsActivity)
                }
            }
        }).callApi()

    }

    private fun setTopBar() {
        binding.topBar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.topBar.imgLogo.visibility = View.GONE
        binding.topBar.txtTitle.visibility = View.VISIBLE
        binding.topBar.txtTitle.text = resources.getString(R.string.select_sports)
    }

    override fun onGetSportsListSuccess(response: List<CoachSportsListResult?>?) {

        hideLoader()
         selectedSportsId= ArrayList<CoachSportsListResult>()
        if (response!!.isNotEmpty()) {

            if (data!!.isNotEmpty()) {
                response?.forEach {

                    val selectedid = it?.getId()
                    Log.e("TAG", "onGetSportsListSuccess: step 1", )

                    data!!.forEach {dataa->
                        Log.e("TAG", "onGetSportsListSuccess: step 2", )

                        val dataid= dataa?.id
                        Log.e("TAG", "onGetSportsListSuccess: dataid $dataid", )

                        if (selectedid==dataid){
                            Log.e("TAG", "onGetSportsListSuccess: step 3", )
                            dataa?.selectedCoach = true
                        }
                    }
                    selectedSportsId.add(it!!)
                }
                Log.e("TAG", "onGetSportsListSuccess: selected sorts list is ${selectedSportsId[0].getId()}",)
                adapter=SportsGridListAdapter(data!!, this@SelectSportsActivity)
                binding.rcvSportsGridList.adapter = adapter
            } else{
                showToast("data list empty")
            }
        } else{

        }
    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)
    }
}