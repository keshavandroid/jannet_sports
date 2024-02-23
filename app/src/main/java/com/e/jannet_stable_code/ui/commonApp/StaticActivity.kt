package com.e.jannet_stable_code.ui.commonApp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IStaticDataController
import com.e.jannet_stable_code.retrofit.controller.StaticDataController
import com.e.jannet_stable_code.retrofit.staticdata.StaticDataResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IStaticPageView
import kotlinx.android.synthetic.main.activity_static.*
import kotlinx.android.synthetic.main.topbar_layout.*
import java.util.*

class StaticActivity : BaseActivity(), IStaticPageView {
    private var type = "user_id"
    lateinit var controller: IStaticDataController

    companion object {
        fun newIntent(context: Context, type: String): Intent {
            val intent = Intent(context, StaticActivity::class.java)
            //privacy
            intent.putExtra("type", type)
            return intent
        }
    }

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static)

        val id = SharedPrefUserData(this).getSavedData().id
        val token = SharedPrefUserData(this).getSavedData().token

        controller = StaticDataController(this, this)
        type = intent.getStringExtra("type")!!

        if (type == "privacy") {
//            PrivacyPolicyController(this,object:ControllerInterface{
//                override fun onFail(error: String?) {
//
//                }
//
//                override fun <T> onSuccess(response: T, method: String) {
//                    try {
//                        val data=response as PrivacyPolicyResponse
//                        val string= data.getResult()!!.title+"\n"+data.getResult()!!.content
//                        txtPrivacy.text=string
//                    }catch (e:Exception){e.printStackTrace()}
//                }
//            })

            controller.callStaticDataApi("1", id, token)
            showLoader()

        } else if (type == "terms") {

            controller.callStaticDataApi("2", id, token)
            showLoader()

        }

        setTopBar()
    }

    private fun setTopBar() {
        txtTitle.visibility = View.VISIBLE
        imgBack.visibility = View.GONE
        closeBack.visibility = View.VISIBLE

        var title = type
        txtTitle.text = title

        closeBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onStaticDataSuccess(Response: StaticDataResult) {
        hideLoader()


        txtTitle.text = Response.getTitle().toString()
        txtPrivacy.text = Response.getContent().toString()




    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }
}