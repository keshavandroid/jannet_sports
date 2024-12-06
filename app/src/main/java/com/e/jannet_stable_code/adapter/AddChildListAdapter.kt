package com.e.jannet_stable_code.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.response.ChildListObject
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.ui.loginRegister.addChildScreen.AddChildActivity
import com.e.jannet_stable_code.utils.DatePickerResult
import com.e.jannet_stable_code.utils.MultiSpinner
import com.e.jannet_stable_code.utils.datePicker
import com.e.jannet_stable_code.utils.getPathFromUri
import com.google.gson.GsonBuilder


private const val TAG = "AddChildListAdapter"

class AddChildListAdapter() : RecyclerView.Adapter<AddChildListAdapter.ChildListAdapterVH>() {

    var arrayList: ArrayList<ChildListObject> = ArrayList()
    lateinit var context: AddChildActivity
    var addChildlistInterface: AddChildlistInterface? = null

    var closeImageFlag = false

    var minGrade = 1
    var maxGrade = 15


    constructor(
        arrayList: ArrayList<ChildListObject>,
        context: AddChildActivity,
        addChildlistInterface: AddChildlistInterface,
    ) : this() {
        this.arrayList = arrayList
        this.addChildlistInterface = addChildlistInterface
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListAdapterVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_child_list, parent, false)

        return ChildListAdapterVH(view)
    }

    fun getListForApiCall(): ArrayList<AddChildActivity.ChildObject> {
        val list = ArrayList<AddChildActivity.ChildObject>()
        for (i in arrayList.indices) {
            val item = AddChildActivity.ChildObject()
            //arrayList[i].gradeId
            item.setData(
                arrayList[i].firstName,
                arrayList[i].middleName,
                arrayList[i].lastName,
                arrayList[i].jurseyName,
                arrayList[i].gradeId,
                arrayList[i].email,
                arrayList[i].bdate,
                arrayList[i].sportsIDS,
                context.getPathFromUri(arrayList[i].imageUri),
                arrayList[i].gender,
                arrayList[i].pwd
            )
            item.setChildId1(arrayList[i].id)
            list.add(item)
        }
        return list
    }

    override fun getItemCount(): Int {
        closeImageFlag = arrayList.size > 1
        return arrayList.size
    }

    var imageTop: ImageView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildListAdapterVH, position: Int) {

        if (editFlag) {

            holder.etxtChildName.setText(arrayList.get(position).firstName.toString())
            holder.txtPassword.visibility = View.GONE
            holder.llPwd.visibility = View.GONE
            holder.etxtEmail.isEnabled = false
            holder.etxtEmail.isClickable = false
            holder.etxtEmail.isActivated = false
            holder.etxtEmail.isFocusable = false

        }

        Log.d(TAG, "test adapter>>: $closeImageFlag")
        holder.imgClose.visibility = if (closeImageFlag) View.VISIBLE
        else View.GONE

        if (arrayList[position].imageUri == null) {
            holder.imgChildProfile.setImageResource(R.mipmap.cam)

            Glide.with(context)
                .load(arrayList[position].imageUrl)
                .placeholder(R.mipmap.cam)
                .error(R.mipmap.cam)
                .into(holder.imgChildProfile)

        } else Glide.with(context)
            .load(arrayList[position].imageUri)
            .placeholder(R.mipmap.cam)
            .error(R.mipmap.cam)
            .into(imageTop!!)

        holder.etxtChildName.setText(arrayList[position].firstName)

        //ew param
        holder.etxtChldMiddleName.setText(arrayList[position].middleName)
        holder.etxtChildLastName.setText(arrayList[position].lastName)
        holder.etxtJerseyName.setText(arrayList[position].jurseyName)
//        holder.grade.setText(arrayList[position].grade)
//

        holder.etxtEmail.setText(arrayList[position].email)
        holder.etxtPwd.setText(arrayList[position].pwd)
        holder.txtBirthdate.text = arrayList[position].bdate
        holder.txtSportsSelected.text = arrayList[position].sports
        holder.grade.text = arrayList[position].gradeId
        holder.paidamount.setText(arrayList[position].addedChildamount)
        var allowChildPayment = 0
        holder.checkbox.setOnClickListener {
            if (allowChildPayment == 0) {
                holder.checkbox.setImageResource(R.mipmap.check1)

                allowChildPayment = 1

                holder.lladdamount.isVisible = true


            } else if (allowChildPayment == 1) {

                holder.checkbox.setImageResource(R.mipmap.check2)
                allowChildPayment = 0

                holder.lladdamount.isGone = true
            }
        }
        if (arrayList[position].gender == "m") {
            holder.imgMale.setImageResource(R.mipmap.rad)
            holder.imgFemale.setImageResource(R.mipmap.rad1)
        } else if (arrayList[position].gender == "f") {
            holder.imgMale.setImageResource(R.mipmap.rad1)
            holder.imgFemale.setImageResource(R.mipmap.rad)
        }

        holder.show_hide.setOnClickListener {


        }
        holder.show_hide.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_UP -> holder.etxtPwd.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    MotionEvent.ACTION_DOWN -> holder.etxtPwd.setInputType(InputType.TYPE_CLASS_TEXT)
                }
                return true

            }

        })


    }


    private fun removeItem(position: Int) {
        try {
            Log.d(TAG, "removeItem: test0>>" + arrayList.size)
            arrayList.removeAt(position)
            notifyDataSetChanged()
            Log.d(TAG, "removeItem: test1>>" + arrayList.size)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun setImageToPos(positionAdapter: Int, uri: Uri) {
        if (imageTop != null)
            Glide.with(context)
                .load(uri)
                .into(imageTop!!)
        arrayList[positionAdapter].imageUri = uri
    }

    fun addNewItem() {
        arrayList.add(ChildListObject())
        notifyDataSetChanged()
    }

    var sportsListTop = ArrayList<SportsListResponse.Result>()
    var sportsListStings = ArrayList<String>()

    fun setSportsList(sportsList: List<SportsListResponse.Result?>) {
        for (i in sportsList.indices) {
            try {
                sportsListTop.add(sportsList[i]!!)
                sportsListStings.add(sportsList[i]!!.sportsName!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun setGradeList(gradeList: GradeListResult) {
//
//        try {
//
//
//        } catch (e: Exception) {
//
//
//        }
//
//    }

    var editFlag = false

    fun setEditChildUserData(data: ChildListObject?) {

        Log.d(TAG, "ChildAdapterData: ==>>" + GsonBuilder().setPrettyPrinting().create().toJson(data))

        Log.d(TAG, "setEditChildUserData: et==>>" + data!!.id + ">>middlename" + data.middleName + ">>" + data.firstName + ">>" + data.email + ">>" + data.bdate + ">>" + data.imageUrl + ">>" + data.imageUri)
        editFlag = true
        arrayList.clear()
        arrayList.add(data!!)
        notifyDataSetChanged()
    }

    inner class ChildListAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgChildProfile: ImageView = itemView.findViewById(R.id.imgChildProfile)
        val imgClose: ImageView = itemView.findViewById(R.id.imgClose)
        val etxtChildName: EditText = itemView.findViewById(R.id.etxtChildName)
        val etxtChldMiddleName: EditText = itemView.findViewById(R.id.etxtChilMiddleName)
        val etxtChildLastName: EditText = itemView.findViewById(R.id.etxtChilLastName)

        val etxtJerseyName: EditText = itemView.findViewById(R.id.etxtChilJerseyName)
        val etxtEmail: EditText = itemView.findViewById(R.id.etxtEmail)
        val etxtPwd: EditText = itemView.findViewById(R.id.etxtPwd)
        val txtBirthdate: TextView = itemView.findViewById(R.id.txtBirthdate)
        val grade: TextView = itemView.findViewById(R.id.txtGradeSelected)
        val txtSportsSelected: TextView = itemView.findViewById(R.id.txtSportsSelected)
        val txtPassword: TextView = itemView.findViewById(R.id.txtPassword)
        private val multispinner: MultiSpinner = itemView.findViewById(R.id.multispinner)
        val llPwd: LinearLayout = itemView.findViewById(R.id.llPwd)
        private val gradeSpinnr: Spinner = itemView.findViewById(R.id.spGrade)
        val imgMale: ImageView = itemView.findViewById(R.id.imgMale)
        val imgFemale: ImageView = itemView.findViewById(R.id.imgFemale)
        val txtMale: TextView = itemView.findViewById(R.id.txtMale)
        val txtFemale: TextView = itemView.findViewById(R.id.txtFemale)
        val checkbox: ImageView = itemView.findViewById(R.id.img_child_permision)
        val paidamount: EditText = itemView.findViewById(R.id.etxt_child_payment)
        val lladdamount: LinearLayout = itemView.findViewById(R.id.llchild_permission)
        val show_hide: ImageView = itemView.findViewById(R.id.iv_eye)

        init {
            imgMale.setOnClickListener {
                imgMale.setImageResource(R.mipmap.rad)
                imgFemale.setImageResource(R.mipmap.rad1)
                arrayList[adapterPosition].gender = "m"
            }
            imgFemale.setOnClickListener {
                imgMale.setImageResource(R.mipmap.rad1)
                imgFemale.setImageResource(R.mipmap.rad)
                arrayList[adapterPosition].gender = "f"
            }
            txtMale.setOnClickListener { imgMale.performClick() }
            txtFemale.setOnClickListener { imgFemale.performClick() }

            imgClose.setOnClickListener { removeItem(adapterPosition) }

            imgChildProfile.setOnClickListener {
                imageTop = imgChildProfile
                addChildlistInterface!!.onImagePickSelected(adapterPosition)
            }
            etxtChildName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    arrayList[adapterPosition].firstName = etxtChildName.text.toString().trim()
                }
            })

            etxtChldMiddleName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    arrayList[adapterPosition].middleName = etxtChldMiddleName.text.toString()

                }

                override fun afterTextChanged(s: Editable?) = Unit

            })

            //2
            etxtChildLastName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    arrayList[adapterPosition].lastName = etxtChildLastName.text.toString()

                }

                override fun afterTextChanged(s: Editable?) = Unit

            })
            //3

            etxtJerseyName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    arrayList[adapterPosition].jurseyName = etxtJerseyName.text.toString()

                }

                override fun afterTextChanged(s: Editable?) = Unit

            })
            //4

//            grade.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int,
//                ) = Unit
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                    arrayList[adapterPosition].grade = grade.text.toString()
//
//                }
//
//                override fun afterTextChanged(s: Editable?) = Unit
//
//            })


            etxtEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    arrayList[adapterPosition].email = etxtEmail.text.toString()
                }
            })
            etxtPwd.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    arrayList[adapterPosition].pwd = etxtPwd.text.toString()
                }
            })
            paidamount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    arrayList[adapterPosition].addedChildamount = paidamount.text.trim().toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }


            })

            txtBirthdate.setOnClickListener {
                datePicker(
                    txtBirthdate,
                    context,
                    context.supportFragmentManager,
                    object : DatePickerResult {
                        override fun onSuccess(string: String) {
                            arrayList[adapterPosition].bdate = string
                        }
                    }, true, futureDatesOnly = false
                )
            }

            grade.setOnClickListener {

                gradeSpinnr.performClick()

            }

            val tempGradeList = ArrayList<String?>()

            for (i in minGrade.toInt()..maxGrade.toInt()) {

                tempGradeList.add("$i")
            }
            val adapterGradeRange: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_item,
                    tempGradeList
                )
            adapterGradeRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            gradeSpinnr.adapter = adapterGradeRange

            gradeSpinnr.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        try {
                            arrayList[adapterPosition].gradeId = tempGradeList[position].toString()
                            grade.text = tempGradeList[position]

                        } catch (e: Exception) {
                            e.printStackTrace()
                            grade.text = ""
                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                        grade.text = ""
                    }
                }




            txtSportsSelected.setOnClickListener {
                multispinner.setDataList(sportsListStings)
                multispinner.performClick()
            }
            multispinner.setMultiSpinnerListener {
                var string = ""
                val list = it
                arrayList[adapterPosition].sports = ""
                Log.d(TAG, "testMultispinner: Size>>" + list.size)
                for (i in list.indices) {
                    try {
                        if (list[i]) {
                            if (string == "") {
                                string = sportsListTop[i].sportsName!!
                                arrayList[adapterPosition].sportsIDS =
                                    sportsListTop[i].id.toString()
                            } else {
                                string += ", " + sportsListTop[i].sportsName!!
                                arrayList[adapterPosition].sportsIDS += ", " + sportsListTop[i].id.toString()
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                arrayList[adapterPosition].sports = string
                txtSportsSelected.text = string
            }

        }
    }

    interface AddChildlistInterface {
        fun onImagePickSelected(position: Int)
    }
}