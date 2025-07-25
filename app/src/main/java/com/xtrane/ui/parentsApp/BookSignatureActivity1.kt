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
import com.xtrane.R
import com.xtrane.canvasLib.CanvasView
import com.xtrane.databinding.ActivityBookSignatureBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.controller.BookChildEventController
import com.xtrane.retrofit.controller.BookEventController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.JoinTeamFromParentController
import com.xtrane.retrofit.response.PaymentIntentResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.ImageFilePath
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.RegisterControllerInterface
import com.google.gson.GsonBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier


class BookSignatureActivity1 : BaseActivity(), RegisterControllerInterface, ControllerInterface {
    var canvasView: CanvasView? = null
    lateinit var controller: BookChildEventController
    lateinit var childController: BookEventController
    private var imageUri: Uri? = null
    lateinit var joinTeamController: JoinTeamFromParentController
    private var TAG = "BookSignatureActivity"
    private var id = ""
    private var token = ""

    //PAYMENT STRIPE
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String
    private var stripe: Stripe? = null
    private var alertCheckbox: CheckBox?=null
    private lateinit var binding: ActivityBookSignatureBinding

    override fun getController(): IBaseController? {
        return null
    }

    //PAYMENT STRIPE
    private fun fetchPaymentIntentApi(fees: String?) {

        id = SharedPrefUserData(this).getSavedData().id!!
        val eventid = intent.getStringExtra("eventId")

        Log.e("Params:id=",id+"fees="+fees+"eventid="+eventid+"currency="+"usd"+"Bookevent="+"Bookevent")
        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().createPaymentIntentApi(id,fees,eventid,"usd","Bookevent")

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: PaymentIntentResponse = gson.fromJson(reader, PaymentIntentResponse::class.java)

                    if (response.getStatus()== 1) {
                        val data = response.getMessage()
                        Utilities.showToast(this@BookSignatureActivity1, response.getMessage())

                        if(!response.getPaymentIntentClientSecret().isNullOrEmpty()){
                            paymentIntentClientSecret = response.getPaymentIntentClientSecret().toString()

                            runOnUiThread {binding.txtBook.setEnabled(true) }

//                            PaymentConfiguration.init(this@BookSignatureActivity, response.getPaymentIntentClientSecret().toString())
                        }
                    } else {
                        Utilities.showToast(this@BookSignatureActivity1, response.getMessage())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Log.d("TAG", "onError: ===" + error)
            }
        })

    }

    private fun onPayClicked() {
        val configuration = PaymentSheet.Configuration("Example, Inc.")

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }

    //PAYMENT STRIPE
    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.d("TAG00", "Canceled")
            }
            is PaymentSheetResult.Failed -> {
                Log.d("TAG00", "onError: === ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                Log.d("TAG00", "Completed")
                afterPaymentSuccess()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_book_signature)
        binding = ActivityBookSignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alertCheckbox = findViewById(R.id.alertCheckbox)

        //Initialize PAYMENT STRIPE

        //TEST KEY
        PaymentConfiguration.init(
            this,
            "pk_test_51RMt1EQOkb1porNanZl25YYGKxBAVSBAsYMixSUBNexFAk2VOJZYgmpVOGeie4VEsFh1E843XKHU3ot9wd8J7VJ500QXtihzAf"
        );
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        //OLD KEY
//        pk_test_51JQCe5SEJyzuwCwwZsb9bqrkeGh6liULU7KtwJmvySO75ASyiN7MEyhhY4Cc90OlauNFUTwsg7mj4XlPonNCYwvj00dUQSyXLD

        var eventID = intent.getStringExtra("eventId")
        setTopBar()
        controller = BookChildEventController(this, this)
        joinTeamController = JoinTeamFromParentController(this, this)
        childController = BookEventController(this, this)
        binding.imgPrivacy.setOnClickListener {
            privacyClicked()
        }
        binding.txtPrivacy.setOnClickListener {   binding.imgPrivacy.performClick() }
        binding. txtClear.setOnClickListener { canvasView!!.clearCanvas() }

        canvasView = CanvasView(this)
        binding.rlSign.addView(canvasView)
//        Log.e("TAG", "onCreate:booksignature  ${rlSign.drawToBitmap()}")

        val coachId = intent.getStringExtra("Coach_id")
        val fees = intent.getStringExtra("Fees")
        val team_id = intent.getStringExtra("Team_id")
        val parentJoin = intent.getStringExtra("COACH_JOIN")

        binding.tvFees.text=fees

        // Hook up the pay button

        // Hook up the pay button

        binding.txtBook.setEnabled(false)
        paymentSheet = PaymentSheet(this) { paymentSheetResult: PaymentSheetResult? ->
            onPaymentSheetResult(
                paymentSheetResult!!
            )
        }
        fetchPaymentIntentApi(fees)

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

                if(alertCheckbox!!.isChecked){
                    Toast.makeText(this@BookSignatureActivity1,
                        "Pay the amount on field",
                        Toast.LENGTH_SHORT).show()
                    afterPaymentSuccess()
                }else{
                    onPayClicked()
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

    fun afterPaymentSuccess(){
        imageUri = getImageUri(getBitmapFromView(binding.rlSign)!!)

        val eventid = intent.getStringExtra("eventId")
        val childId = intent.getStringExtra("ChildId")
        val fess = intent.getStringExtra("Fees")
        Log.e("TAG", "onCreate:booksignatur1  ${fess + childId + eventid}")

        var registerData = RegisterData()
        registerData.child_id = childId.toString()
        registerData.fees = fess.toString()
        registerData.event_id = eventid.toString()
        registerData.image = ImageFilePath.getPath(this, imageUri!!)!!

        controller.bookEvent(registerData)

        Log.e("ChildData", childId.toString() + "" + fess.toString() + " " + eventid.toString() + " ")
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
            Toast.makeText(this@BookSignatureActivity1,
                "You have not allow to book event",
                Toast.LENGTH_SHORT).show()
        } else {

            if(userType.equals("child",ignoreCase = true)){
                binding.txtSendRequestParent.visibility = View.VISIBLE
                Toast.makeText(this@BookSignatureActivity1, "Send Request To Parent", Toast.LENGTH_SHORT)
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
        Toast.makeText(this@BookSignatureActivity1, response.toString(), Toast.LENGTH_SHORT).show()
    }
}