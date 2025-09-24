package com.xtrane.ui.parentsApp

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.xtrane.R
import com.xtrane.adapter.EventBookMatchListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityBookeEventBinding
import com.xtrane.retrofit.APIClient.SERVER_URL
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.*
import com.xtrane.retrofit.matchlistdata.MatchListResult
import com.xtrane.retrofit.response.GetProfileParentApiResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.ImageFilePath
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.viewinterface.IBookTickitParentView
import com.xtrane.viewinterface.IMatchListView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class BookeEventActivity : BaseActivity(), IBookTickitParentView, IMatchListView,
    EventBookMatchListAdapter.ISelectMatchClickListner {
    private lateinit var adapter: EventBookMatchListAdapter
    var matchListResponse: ArrayList<MatchListResult?>? = ArrayList()
    lateinit var iBookTicketParentController: IBookTicketParentController
    var matchId = ""
    var ttltickits = "1"

    //lateinit var controller1: MatchListBookEventController
    lateinit var controller: IMatchListController
    var successtotAmount: Int = 0
    private var bookPaymentType: String? = null
    private var paymentIntentClientSecret: String? = null
    private lateinit var paymentSheet: PaymentSheet

    var id = ""
    var token = ""
    var eventID = ""
    var fees = ""
    var eid = ""
    lateinit var etxt_your_Name: TextView
    lateinit var etxt_contact: TextView
    private lateinit var binding: ActivityBookeEventBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookeEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_booke_event)

        id = SharedPrefUserData(this).getSavedData().id!!
        token = SharedPrefUserData(this).getSavedData().token!!
        eventID = intent.getStringExtra("EVENT_id").toString()
        fees = intent.getStringExtra("fees").toString()

        controller = MatchListController(this, this)
        id = SharedPrefUserData(this).getSavedData().id!!
        token = SharedPrefUserData(this).getSavedData().token!!

        iBookTicketParentController = BookTicketParentController(this, this)

        setTopBar()
        var txt_amount = findViewById<TextView>(R.id.txt_amount)
        etxt_your_Name = findViewById<TextView>(R.id.etxt_your_Name)
        etxt_contact = findViewById<TextView>(R.id.etxt_contact)
        val chkPertiMatch = findViewById<View>(R.id.chkPertiMatch) as CheckBox

        binding.etxtNoTickets.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun afterTextChanged(p0: Editable?) {

                if (binding.etxtNoTickets.text.trim().toString().isNullOrEmpty()) {
//                    etxtNo_tickets.setText("0")
                    txt_amount.setText("0")

                } else {

                    ttltickits = binding.etxtNoTickets.text.trim().toString()

                    var amt = successtotAmount.toInt() * ttltickits.toInt()
                    txt_amount.setText(amt.toString())

                    if (adapter != null)
                        selectMatch(adapter.getMatchList()!!, true)
                }
            }

        })
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

//        if (matchListResponse!!.isEmpty()) {
//            binding.lrmatch.visibility = View.GONE
//        } else {
//            binding.lrmatch.visibility = View.VISIBLE
//        }

        binding.cardWallet.setOnClickListener {
            bookPaymentType = "wallet"
            binding.cardWallet.setCardBackgroundColor(resources.getColor(R.color.stroke_border))
            binding.cardStripe.setCardBackgroundColor(resources.getColor(R.color.white))
//            binding.txtwallet.setTextColor(resources.getColor(R.color.white))
//            binding.textView.setTextColor(resources.getColor(R.color.black))

        }
        binding.cardStripe.setOnClickListener {
            bookPaymentType = "stripe"
            binding.cardStripe.setCardBackgroundColor(resources.getColor(R.color.stroke_border))
            binding.cardWallet.setCardBackgroundColor(resources.getColor(R.color.white))
//            binding.textView.setTextColor(resources.getColor(R.color.white))
//            binding.txtwallet.setTextColor(resources.getColor(R.color.black))

        }
        chkPertiMatch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            var totAmount: Int = 0
            if (!matchListResponse!!.isEmpty()) {
                if (isChecked) {
                    binding.rvMatchList!!.visibility = View.VISIBLE
                    selectMatch(adapter.getMatchList()!!, true)
                } else {
                    matchId = ""
                    binding.rvMatchList!!.visibility = View.GONE
                    for (i in 0..matchListResponse!!.size - 1) {
                        /*  if (matchListResponse!!.get(i)!!.isCheck) {
                              matchListResponse!!.get(i)!!.isCheck = false
                          }*/
                        if (matchId.isEmpty()) {
                            matchId += matchListResponse!!.get(i)!!.getMatchId()
                        } else {
                            matchId += "," + matchListResponse!!.get(i)!!.getMatchId()
                        }

                        if (!binding.etxtNoTickets.text.trim().toString().isNullOrEmpty())
                            if (matchListResponse!!.get(i)!!.getEventPrice() != null)
                                totAmount += matchListResponse!!.get(i)!!.getEventPrice()!!
                                    .toInt() * (binding.etxtNoTickets.text.trim()
                                    .toString()).toInt()
                    }
                    txt_amount.setText(totAmount.toString())
                }
            } else {
                showToast("'Matchlist is not found so ticket booking is unavailable!")

            }

        })
        val storeData = SharedPrefUserData(this)
        val id = storeData.getSavedData().id
        val token = storeData.getSavedData().token
        eid = intent.getStringExtra("EVENT_id").toString()

        binding.txtBookEvent.setOnClickListener {

            if (etxt_your_Name.text.trim().toString().isEmpty()) {

                showToast("'Please Enter Your Name to Continue...")
            } else if (binding.etxtNoTickets.text.trim().toString().isEmpty()) {

                showToast("Please Enter Tickets to Continue...")
            } else if (etxt_contact.text.trim().toString().isEmpty()) {

                showToast("Please Enter Contact Number to Continue...")
            } else if (matchListResponse!!.isEmpty()) {

                showToast("Match is not found. so, booking is unavailable!")
            } else {


                Log.e("TAG", "onCreate: event id in book event activity$eid")

                if (bookPaymentType.equals("stripe")) {
                    if (matchId != null && matchId.length > 0) {

                        showLoader()

                        onPayClicked(fees)

                    }
                    else {

                        showToast("Match is not found. so, booking is unavailable!")

                    }

                } else if (bookPaymentType.equals("wallet")) {

                    if (matchId != null && matchId.length > 0) {

                        showLoader()

                        iBookTicketParentController.callBookTicketApi(
                            id!!,
                            token.toString(),
                            eid.toString(),
                            txt_amount.text.toString(),
                            binding.etxtNoTickets.text.trim().toString(),
                            matchId,
                            etxt_your_Name.text.toString(),
                            etxt_contact.text.toString(),
                            "wallet"
                        )
                    } else {
                        showToast("Match is not found. so, booking is unavailable!")

                    }

                }

            }
        }
    }

    private fun onPayClicked(fees: String?) {

        val userId = SharedPrefUserData(this).getSavedData().id!!
        val eventid = intent.getStringExtra("eventId")

//        val publishableKey =
//            "pk_test_51RMt1EQOkb1porNanZl25YYGKxBAVSBAsYMixSUBNexFAk2VOJZYgmpVOGeie4VEsFh1E843XKHU3ot9wd8J7VJ500QXtihzAf"

        val publishableKey = "pk_live_51RLyQDBuyQ1GNDW7vRJvbEgJsSEJz84SVekUx58lTBw92QE1yHfj0KvSy4Vb5R4VvUnA3NgO6Ju4EV08FLNOy1RE00Q8I4qqUq"

        PaymentConfiguration.init(this@BookeEventActivity, publishableKey)
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                val url =
                    URL(SERVER_URL + "createPaymentIntent?userId=" + userId + "&amount=" + fees + "&currency=USD" + "&eventId=" + eventid + "&bookType=Bookevent")
                Log.e("startCheckout", "url: $url")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                val stream = conn.inputStream.bufferedReader().use { it.readText() }
                JSONObject(stream)
            }

            if (response != null && response.length() > 0) {
                Log.e("onPaymentSheet=", "$response")

                paymentIntentClientSecret = response.getString("payment_intent_client_secret")
                Log.e("paymentIntentClientSecret=", "$paymentIntentClientSecret")

                presentPaymentSheet()
            }

        }

    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret!!,
            PaymentSheet.Configuration(
                merchantDisplayName = "Giovanni Trane",
                // Set `allowsDelayedPaymentMethods` to true if your business handles
                // delayed notification payment methods like US bank accounts.
                allowsDelayedPaymentMethods = true
            )
        )

    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                // Payment successful
                Log.e("TAG", "onPaymentSheetResult:Payment succeeded")

                Toast.makeText(this, "Payment succeeded", Toast.LENGTH_LONG).show()
                afterPaymentSuccess()
            }

            is PaymentSheetResult.Canceled -> {
                Log.e("TAG", "onPaymentSheetResult:Payment canceled")

                Toast.makeText(this, "Payment canceled", Toast.LENGTH_LONG).show()
            }

            is PaymentSheetResult.Failed -> {

                Log.e("TAG", "onPaymentSheetResult: ${paymentSheetResult.error.localizedMessage}")

                Toast.makeText(
                    this,
                    "Payment failed: ${paymentSheetResult.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun afterPaymentSuccess() {

        iBookTicketParentController.callBookTicketApi(
            id,
            token.toString(),
            eid.toString(),
            fees,
            binding.etxtNoTickets.text.trim().toString(),
            matchId,
            etxt_your_Name.text.toString(),
            etxt_contact.text.toString(),
            "stripe"
        )

    }

    override fun onResume() {
        super.onResume()

        GetProfileController(this, true, object : ControllerInterface {
            override fun onFail(error: String?) {
                hideLoader()
            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as GetProfileParentApiResponse
                    setUserData(data.getResult()!![0]!!)
                    hideLoader()
                    controller.callMatchListApi(id, token, eventID.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    hideLoader()
                }
            }
        })
    }

    private fun setTopBar() {
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text = "BOOK EVENT"
    }

    private fun setUserData(result: GetProfileParentApiResponse.Result) {

        binding.etxtYourName.setText(result.firstname + " " + result.middleName + " " + result.lastname)

        binding.etxtContact.setText(result.contactNo)

    }

    override fun onBookTicketSuccessful(msg: String) {
        var dialog = Dialog(this, R.style.MyBottomSheetDialogTheme)
        val dialogview = LayoutInflater.from(this)
            .inflate(R.layout.dlg_confirm, null, false)
        val tv_title = dialogview.findViewById<TextView>(R.id.tv_title)
        val tv_ok = dialogview.findViewById<TextView>(R.id.tv_ok)
        tv_title.setText(msg)
        tv_ok.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            onBackPressed()
            finish()

        })
        dialog?.setCancelable(true)
        dialog?.setContentView(dialogview)
        dialog?.show()
        //showToast("Ticket Booked Successful ")
        hideLoader()

    }

    override fun onBookTicketFailed(msg: String) {
        hideLoader()

        Toast.makeText(this@BookeEventActivity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onMatchListSuccess(response: ArrayList<MatchListResult?>?) {
        hideLoader()
        matchListResponse!!.addAll(response!!)
        successtotAmount = 0
        matchId = ""
        for (i in 0..matchListResponse!!.size - 1) {
            if (matchId.isEmpty()) {
                matchId += matchListResponse!!.get(i)!!.getMatchId()
            } else {
                matchId += "," + matchListResponse!!.get(i)!!.getMatchId()
            }
            if (matchListResponse!!.get(i)!!.getEventPrice() != null)
                successtotAmount += matchListResponse!!.get(i)!!.getEventPrice()!!.toInt()

        }
        binding.txtAmount.setText(successtotAmount.toString())
        adapter = EventBookMatchListAdapter(this, response)
        adapter.iSelectClickListner = this
        binding.rvMatchList.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }

    override fun selectMatch(matchList: List<MatchListResult?>, isCheck: Boolean) {
        var tot: Int = 0
        matchId = ""
        if (!binding.etxtNoTickets.text.toString().isNullOrEmpty())
            for (i in 0..matchList!!.size - 1) {
                if (matchList!!.get(i)!!.isCheck) {

                    if (matchList!!.get(i)!!.getEventPrice() == null) {
                        tot += 0 * (binding.etxtNoTickets.text.trim().toString()).toInt()
                    } else {
                        tot += matchList!!.get(i)!!.getEventPrice()!!
                            .toInt() * (binding.etxtNoTickets.text.trim().toString()).toInt()
                    }


                    if (matchId.isEmpty()) {
                        matchId += matchList.get(i)!!.getMatchId()
                    } else {
                        matchId += "," + matchList.get(i)!!.getMatchId()
                    }
                }
            }

        binding.txtAmount.setText(tot.toString())
    }

}