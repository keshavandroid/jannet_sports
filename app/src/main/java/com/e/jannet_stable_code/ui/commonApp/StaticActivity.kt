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


            txtPrivacy.text = "Privacy Policy for X-Trane\n" +
                    "Effective Date: October 9th 2024\n" +
                    "\n" +
                    "Thank you for choosing X-Trane. This Privacy Policy explains how we collect, use, and protect your personal information when you use our mobile application (the \"App\"). We are committed to protecting your privacy and ensuring that your personal data is handled securely and responsibly.\n" +
                    "\n" +
                    "1. Information We Collect\n" +
                    "We collect and process the following types of personal data:\n" +
                    "\n" +
                    "Names: This is used to personalize the app and improve your user experience.\n" +
                    "Phone Numbers: This is used for identification, authentication, and to facilitate communication between users (such as coaches and kids/parents).\n" +
                    "2. How We Use Your Information\n" +
                    "We use the collected data for the following purposes:\n" +
                    "\n" +
                    "To create and manage user accounts.\n" +
                    "To allow communication between coaches and participants (kids and their parents) for scheduling and organizing sports activities.\n" +
                    "To send notifications regarding events, updates, and necessary reminders related to sports activities.\n" +
                    "To ensure the security and integrity of the App and prevent unauthorized access.\n" +
                    "3. Legal Basis for Processing (GDPR)\n" +
                    "For users within the European Economic Area (EEA), we process your personal data based on the following legal grounds:\n" +
                    "\n" +
                    "Consent: When you provide your personal information (name and phone number), you consent to the processing of this data for the purposes listed above.\n" +
                    "Legitimate Interests: We may process your information for legitimate business purposes, such as improving user experience, provided this doesn’t override your rights and freedoms.\n" +
                    "4. How We Share Your Information\n" +
                    "We do not sell or rent your personal information to third parties. However, we may share your data in the following situations:\n" +
                    "\n" +
                    "With service providers: We may share data with trusted third-party service providers who perform services on our behalf, such as hosting, data storage, or customer support. These service providers are bound by strict confidentiality agreements.\n" +
                    "Legal requirements: We may disclose your data if required to do so by law or in response to a valid request from a governmental authority.\n" +
                    "5. Data Retention\n" +
                    "We will retain your personal data for as long as necessary to fulfill the purposes for which it was collected, or as required by law. You can request the deletion of your personal data at any time (see Section 8: \"Your Rights\").\n" +
                    "\n" +
                    "6. Data Security\n" +
                    "We implement appropriate technical and organizational measures to protect your personal data from unauthorized access, disclosure, or destruction. These measures include encryption, secure storage, and regular security reviews.\n" +
                    "\n" +
                    "However, no system is completely secure, and we cannot guarantee the absolute security of your information. We encourage you to take steps to protect your personal data by keeping your device and login information secure.\n" +
                    "\n" +
                    "7. Children’s Privacy\n" +
                    "The App is intended for use by children under the supervision of their parents or guardians. We do not knowingly collect personal information from children under the age of 16 without verifiable parental consent. If we learn that we have inadvertently collected such information, we will take steps to delete it as soon as possible.\n" +
                    "\n" +
                    "8. Your Rights\n" +
                    "As a user, you have the following rights concerning your personal data:\n" +
                    "\n" +
                    "Access: You have the right to request a copy of the personal data we hold about you.\n" +
                    "Rectification: You may request corrections to any inaccurate or incomplete data.\n" +
                    "Deletion: You can request the deletion of your data, subject to legal obligations that may require us to retain certain information.\n" +
                    "Restriction: You may request that we restrict the processing of your personal data under certain circumstances.\n" +
                    "Portability: You have the right to request the transfer of your personal data to another service provider.\n" +
                    "Objection: You may object to the processing of your personal data in certain circumstances, including for marketing purposes.\n" +
                    "If you are a resident of California, you have additional rights under the CCPA, including:\n" +
                    "\n" +
                    "The right to know what personal data is being collected about you.\n" +
                    "The right to request deletion of your personal data.\n" +
                    "The right to opt-out of the sale of your personal information (note: we do not sell your data).\n" +
                    "The right not to be discriminated against for exercising your rights.\n" +
                    "To exercise any of these rights, please contact us at [insert contact email]. We will respond to your request within the legally mandated time frame.\n" +
                    "\n" +
                    "9. Third-Party Links\n" +
                    "The App may contain links to third-party websites or services. We are not responsible for the privacy practices or content of such third parties. We encourage you to read their privacy policies before providing any personal information.\n" +
                    "\n" +
                    "10. Changes to this Privacy Policy\n" +
                    "We reserve the right to update this Privacy Policy from time to time to reflect changes in our practices or applicable laws. When we make changes, we will notify you by updating the \"Effective Date\" at the top of this policy and providing additional notice where necessary.\n" +
                    "\n" +
                    "11. Contact Us\n" +
                    "If you have any questions or concerns about this Privacy Policy or how we handle your personal data, please contact us at:\n" +
                    "\n" +
                    "X-Trane\n" +
                    "Email: giovanni.delarivamarshall@tchsprogress.com\n" +
                    "\n" +
                    "By using the X-Trane app, you agree to this Privacy Policy."


            //Show data privacy policy from API
            /*controller.callStaticDataApi("1", id, token)
            showLoader()*/

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

        if(title.equals("privacy")){
            txtTitle.text = "Privacy Policy"
        }else if (title.equals("terms")){
            txtTitle.text = "Terms and Conditions"
        }

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