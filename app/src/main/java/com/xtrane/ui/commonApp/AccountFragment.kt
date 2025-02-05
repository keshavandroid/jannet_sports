package com.xtrane.ui.commonApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.xtrane.R
import com.xtrane.databinding.FragmentAccountBinding
import com.xtrane.databinding.FragmentMatchBinding
import com.xtrane.ui.coachApp.CoachMainActivity
import com.xtrane.ui.coachApp.coachUpdateProfileScreen.CoachUpdateProfileActivity
import com.xtrane.ui.loginRegister.SelectSportsActivity
import com.xtrane.ui.loginRegister.UserTypeSelectionActivity
import com.xtrane.ui.parentsApp.EventRegisterRequestActivity
import com.xtrane.ui.parentsApp.ParentUpdateProfileActivity
import com.xtrane.ui.parentsApp.ParentsMainActivity
import com.xtrane.ui.parentsApp.TicketBookingRequest
import com.xtrane.utils.Constants
import com.xtrane.utils.Dialogs
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData

import kotlin.math.log


class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }
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
                binding.lleventRegisterRequest.isVisible = true
                binding.llTicketBookingRequest.isVisible = true
                binding.llselectSports.isVisible = true
            }else if (userType.equals("child")){
                binding.llselectSports.isGone = true
                binding.lleventRegisterRequest.isGone = true
                binding.llTicketBookingRequest.isGone = true
            }else if (userType.equals("parent")){
                binding.llselectSports.isGone=true
                binding.lleventRegisterRequest.isVisible = true
                binding.llTicketBookingRequest.isVisible = true
            }else if(userType.equals("adult")){
                binding.llselectSports.isGone=true
                binding.lleventRegisterRequest.isGone = true
                binding.llTicketBookingRequest.isGone = true
            }
        }



        binding.llselectSports.setOnClickListener {

            val intent = Intent(requireActivity(), SelectSportsActivity::class.java)
            intent.putExtra("COACH","coach")
            startActivity(intent)

        }

        binding.llChangePwd.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ChangePasswordActivity::class.java
                )
            )
        }
        binding.llProfile.setOnClickListener {
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

        binding.llNotifications.setOnClickListener {
            startActivity(
                    Intent(
                            requireActivity(),
                            NotificationsActivity::class.java
                    )
            )
        }

        binding.llLogout.setOnClickListener {
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
        binding.llPrivacyPolicy.setOnClickListener {
            startActivity(StaticActivity.newIntent(requireContext(), "privacy"))
        }

        binding.llTermsCondition.setOnClickListener {
            startActivity(StaticActivity.newIntent(requireContext(), "terms"))


        }

        binding.llContactUs.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "giovanni.delarivamarshall@tchsprogress.com", null
                )
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))

        }

        binding.lleventRegisterRequest.setOnClickListener {

            val intent = Intent(requireContext(),EventRegisterRequestActivity::class.java)
            startActivity(intent)
        }

        binding.llTicketBookingRequest.setOnClickListener {
            val intent = Intent(requireContext(),TicketBookingRequest::class.java)
            startActivity(intent)
        }
    }

    private fun setTopBar() {
        binding.topBar.txtTitle.visibility = View.GONE
        binding.topBar.imgBack.visibility = View.GONE
        binding.topBar.closeBack.visibility = View.VISIBLE

        binding.topBar.closeBack.setOnClickListener {

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