package com.xtrane.ui.parentsApp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.xtrane.R
import com.xtrane.canvasLib.CanvasView
import com.xtrane.databinding.ActivityBookSignatureBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.BookChildEventController
import com.xtrane.retrofit.controller.BookEventController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.JoinTeamFromParentController
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.ImageFilePath
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.viewinterface.RegisterControllerInterface
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.xtrane.retrofit.APIClient.SERVER_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
class BookSignatureActivity : BaseActivity(), RegisterControllerInterface, ControllerInterface {
    var canvasView: CanvasView? = null
    lateinit var controller: BookChildEventController
    lateinit var childController: BookEventController
    private var imageUri: Uri? = null
    lateinit var joinTeamController: JoinTeamFromParentController
    private var TAG = "BookSignatureActivity"
    private var id = ""
    private var token = ""
    private var paymentIntentClientSecret: String? = null
    private lateinit var paymentSheet: PaymentSheet

    //PAYMENT STRIPE
    private var alertCheckbox: CheckBox? = null
    private lateinit var binding: ActivityBookSignatureBinding

    override fun getController(): IBaseController? {
        return null
    }

    //PAYMENT STRIPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_book_signature)
        binding = ActivityBookSignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alertCheckbox = findViewById(R.id.alertCheckbox)

        //Initialize PAYMENT STRIPE

        var eventID = intent.getStringExtra("eventId")
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        setTopBar()
        controller = BookChildEventController(this, this)
        joinTeamController = JoinTeamFromParentController(this, this)
        childController = BookEventController(this, this)
        binding.imgPrivacy.setOnClickListener {
            privacyClicked()
        }
        binding.txtPrivacy.setOnClickListener { binding.imgPrivacy.performClick() }
        binding.txtClear.setOnClickListener { canvasView!!.clearCanvas() }

        canvasView = CanvasView(this)
        binding.rlSign.addView(canvasView)
//        Log.e("TAG", "onCreate:booksignature  ${rlSign.drawToBitmap()}")

        val coachId = intent.getStringExtra("Coach_id")
        val fees = intent.getStringExtra("Fees")
        val parentID = intent.getStringExtra("parentID")
        val team_id = intent.getStringExtra("Team_id")
        val parentJoin = intent.getStringExtra("COACH_JOIN")

        Log.e("fees=",fees+"=")
        binding.tvFees.text = fees

        // Hook up the pay button

        // Hook up the pay button


        binding.txtBook.setOnClickListener {

            if (parentJoin?.trim().toString() == "coach_join") {

                imageUri = getImageUri(getBitmapFromView(binding.rlSign)!!)
                //book team from parent side
                var registerData = RegisterData()
                registerData.coach_id = coachId?.trim().toString()
                registerData.teamId = team_id?.trim().toString()
                registerData.fees = fees?.trim().toString()
                registerData.image = ImageFilePath.getPath(this, imageUri!!)!!
                joinTeamController.joinTeam(registerData)

            } else {
                //call payment api

                if (alertCheckbox!!.isChecked) {
                    Toast.makeText(
                        this@BookSignatureActivity,
                        "Pay the amount on field",
                        Toast.LENGTH_SHORT
                    ).show()

                    afterPaymentSuccess()
                } else {
                    onPayClicked(fees)
                }


            }
        }

        binding.txtSendRequestParent.setOnClickListener {


            id = SharedPrefUserData(this).getSavedData().id!!
            token = SharedPrefUserData(this).getSavedData().token!!
            val eventid = intent.getStringExtra("eventId")
            val childId = intent.getStringExtra("ChildId")

            controller.bookRequestEvent(id, token, childId!!, eventid!!)

        }
    }

    private fun onPayClicked(fees: String?) {

        val userId = SharedPrefUserData(this).getSavedData().id!!
        val eventid = intent.getStringExtra("eventId")


        val publishableKey =
            "pk_test_51RMt1EQOkb1porNanZl25YYGKxBAVSBAsYMixSUBNexFAk2VOJZYgmpVOGeie4VEsFh1E843XKHU3ot9wd8J7VJ500QXtihzAf"
        PaymentConfiguration.init(this@BookSignatureActivity, publishableKey)
        lifecycleScope.launch {

            val response = withContext(Dispatchers.IO) {
                val url = URL(SERVER_URL+"createPaymentIntent?userId=" + userId + "&amount=" + 50 + "&currency=USD" + "&eventId=" + eventid + "&bookType=Bookevent")
                Log.e("startCheckout", "url: $url")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"

                val stream = conn.inputStream.bufferedReader().use { it.readText() }
                JSONObject(stream)
            }

            if (response!=null && response.length()>0)
            {
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
        imageUri = getImageUri(getBitmapFromView(binding.rlSign)!!)

        val eventid = intent.getStringExtra("eventId")
        val childId = intent.getStringExtra("ChildId")
        val fess = intent.getStringExtra("Fees")
        val parentID = intent.getStringExtra("parentID")
        Log.e("TAG", "onCreate:booksignatur1  ${fess + childId + eventid}")

        var registerData = RegisterData()
        registerData.child_id = childId.toString()
        registerData.fees = fess.toString()
        registerData.event_id = eventid.toString()
        registerData.parentID = parentID.toString()
        registerData.image = ImageFilePath.getPath(this, imageUri!!)!!

        controller.bookEvent(registerData)

        Log.e(
            "ChildData",
            childId.toString() + "" + fess.toString() + " " + eventid.toString() + " "
        )
        Log.e("TAG", "onCreate:bookChildSignature  ${ImageFilePath.getPath(this, imageUri!!)!!}")

    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val path = MediaStore.Images.Media.insertImage(
            this.contentResolver,
            inImage,
            "" + System.currentTimeMillis(),
            null
        )
        return Uri.parse(path)
    }

    fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(900, 50, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    var privacyFlag = 0
    private fun privacyClicked() {
        if (privacyFlag == 0) {
            binding.imgPrivacy.setImageResource(R.mipmap.check1)
        } else if (privacyFlag == 1) {
            binding.imgPrivacy.setImageResource(R.mipmap.check2)
        }
        privacyFlag = if (privacyFlag == 0) 1 else 0
    }

    private fun setTopBar() {
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text = getString(R.string.book)
    }

    override fun <T> onSuccess(response: T) {

        val userType = SharedPrefUserData(this).getSavedData().usertype
        if (response?.equals("Participant age is not compatible for this event") == true) {
            Toast.makeText(
                this@BookSignatureActivity,
                "You have not allow to book event",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            if (userType.equals("child", ignoreCase = true)) {
                binding.txtSendRequestParent.visibility = View.VISIBLE
                Toast.makeText(
                    this@BookSignatureActivity,
                    "Send Request To Parent",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }


        /* //Toast.makeText(this@BookSignatureActivity, response.toString(), Toast.LENGTH_SHORT).show()
         if (response?.equals("Participant age is not compatible for this event") == true) {
             txtSendRequestParent.visibility = View.VISIBLE
             Toast.makeText(this@BookSignatureActivity, "You have not allow to book event", Toast.LENGTH_LONG).show()
         } else {
             Toast.makeText(this@BookSignatureActivity, "Successfull", Toast.LENGTH_SHORT).show()
         }*/
    }

    override fun onFail(error: String?) {
        showToast(error)
        Log.d(TAG, "onFail: $error ")
    }

    override fun <T> onSuccess(response: T, method: String) {
        val coach = intent.getStringExtra("child_id").toString()
        val eid = intent.getStringExtra("eventId").toString()

        if (coach == "child") {
            showToast("Book Successful")
            onBackPressed()
            finish()
        } else {
            val intent = Intent(this, EventDetailsActivity::class.java)
            intent.putExtra("eventId", eid)
            startActivity(intent)
            showToast("Book Successful Not ")
            finish()

        }

    }

    override fun <T> onSuccessNew(response: T, method: String) {
        Toast.makeText(this@BookSignatureActivity, response.toString(), Toast.LENGTH_SHORT).show()
    }
}