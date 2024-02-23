package com.e.jannet_stable_code.ui.commonApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.ui.coachApp.CoachMainActivity
import com.e.jannet_stable_code.ui.coachApp.coachUpdateProfileScreen.CoachUpdateProfileActivity
import com.e.jannet_stable_code.ui.loginRegister.SelectSportsActivity
import com.e.jannet_stable_code.ui.loginRegister.UserTypeSelectionActivity
import com.e.jannet_stable_code.ui.parentsApp.EventRegisterRequestActivity
import com.e.jannet_stable_code.ui.parentsApp.ParentUpdateProfileActivity
import com.e.jannet_stable_code.ui.parentsApp.ParentsMainActivity
import com.e.jannet_stable_code.ui.parentsApp.TicketBookingRequest
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.Dialogs
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.topbar_layout.*
import kotlin.math.log


class AccountFragment : Fragment(R.layout.fragment_account) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()

        val storedata = StoreUserData(requireContext())
        val id = storedata.getString(Constants.COACH_ID)
        val token = storedata.getString(Constants.COACH_TOKEN)

        val userType= SharedPrefUserData(requireActivity()).getSavedData().usertype
        Log.d("USERTYPE0", "" +userType)

        //old logic
        /*if (id.trim().toString().isEmpty()){

            llselectSports.isGone=true
            llevent_register_request.isVisible=true
            ll_ticket_booking_request.isVisible=true
        }else{
            llevent_register_request.isVisible=true
            ll_ticket_booking_request.isVisible=true
            llselectSports.isVisible=true
        }*/

        //new logic
        if(userType.isNotEmpty()){
            if (userType.equals("coach")){
                llevent_register_request.isVisible = true
                ll_ticket_booking_request.isVisible = true
                llselectSports.isVisible = true
            }else if (userType.equals("child")){
                llselectSports.isGone = true
                llevent_register_request.isGone = true
                ll_ticket_booking_request.isGone = true
            }else if (userType.equals("parent")){
                llselectSports.isGone=true
                llevent_register_request.isVisible = true
                ll_ticket_booking_request.isVisible = true
            }else if(userType.equals("adult")){
                llselectSports.isGone=true
                llevent_register_request.isGone = true
                ll_ticket_booking_request.isGone = true
            }
        }



        llselectSports.setOnClickListener {

            val intent = Intent(requireActivity(), SelectSportsActivity::class.java)
            intent.putExtra("COACH","coach")
            startActivity(intent)

        }

        llChangePwd.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ChangePasswordActivity::class.java
                )
            )
        }
        llProfile.setOnClickListener {
            val storData = StoreUserData(requireContext())

            if (storData.getString(Constants.COACH_ID).trim().isEmpty() || storData.getString(
                    Constants.COACH_ID
                ).trim() == "" || storData.getString(Constants.COACH_ID).trim() == ""
            ) {

                startActivity(
                    Intent(
                        requireActivity(),
                        ParentUpdateProfileActivity::class.java
                    )
                )

                Log.e("TAG", "onViewCreated: arent logout")
            } else {

                startActivity(
                    Intent(
                        requireActivity(),
                        CoachUpdateProfileActivity::class.java
                    )
                )

                Log.e("TAG", "onViewCreated: coach logout")
            }
//                if(Constants.userType==0){
//                startActivity(
//                    Intent(
//                        requireActivity(),
//                        ParentUpdateProfileActivity::class.java
//                    )
//                )
//            }else if(Constants.userType==1){
//                startActivity(
//                    Intent(
//                        requireActivity(),
//                        CoachUpdateProfileActivity::class.java
//                    )
//                )
//            }

//                            startActivity(
//                    Intent(
//                        requireActivity(),
//                        CoachUpdateProfileActivity::class.java
//                    )
//                )
//

        }

        llNotifications.setOnClickListener {
            startActivity(
                    Intent(
                            requireActivity(),
                            NotificationsActivity::class.java
                    )
            )
        }

        llLogout.setOnClickListener {
            Dialogs.showImageYesNoDialog(requireActivity(), object : Dialogs.DialogResp {
                override fun myDialogResponse(string: String, string1: String) {
                    if (string == "yes") {
                        SharedPrefUserData(requireActivity()).clearAllData()
                        val preferences = StoreUserData(requireContext())
                        preferences.clearData(requireContext())
                        preferences.setString(Constants.COACH_ID, "")
                        preferences.setString(Constants.COACH_TOKEN, "")

                        startActivity(
                            Intent(
                                requireActivity(),
                                UserTypeSelectionActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                }
            })

        }
        llPrivacyPolicy.setOnClickListener {
            startActivity(StaticActivity.newIntent(requireContext(), "privacy"))
        }

        llTermsCondition.setOnClickListener {
            startActivity(StaticActivity.newIntent(requireContext(), "terms"))


        }

        llContactUs.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "email@email.com", null
                )
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))

        }

        llevent_register_request.setOnClickListener {

            val intent = Intent(requireContext(),EventRegisterRequestActivity::class.java)
            startActivity(intent)
        }

        ll_ticket_booking_request.setOnClickListener {
            val intent = Intent(requireContext(),TicketBookingRequest::class.java)
            startActivity(intent)
        }
    }

    private fun setTopBar() {
        txtTitle.visibility = View.GONE
        imgBack.visibility = View.GONE
        closeBack.visibility = View.VISIBLE

        closeBack.setOnClickListener {

            try {
                (requireActivity() as ParentsMainActivity).onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                (requireActivity() as CoachMainActivity).onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}