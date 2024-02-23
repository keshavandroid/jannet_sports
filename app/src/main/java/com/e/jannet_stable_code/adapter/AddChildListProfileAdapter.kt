package com.e.jannet_stable_code.adapter

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.response.ChildListObject

class AddChildListProfileAdapter() :
    RecyclerView.Adapter<AddChildListProfileAdapter.FreshDailyDealVH>() {

    var arrayList: ArrayList<ChildListObject> = ArrayList()
    lateinit var context: Activity
    var addChildlistInterface: AddChildlistInterface? = null

    constructor(
        arrayList: ArrayList<ChildListObject>,
        context: Activity,
        addChildlistInterface: AddChildlistInterface
    ) : this() {
        this.arrayList = arrayList
        this.addChildlistInterface = addChildlistInterface
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreshDailyDealVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child_list_profile, parent, false)

        return FreshDailyDealVH(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    private val TAG = "AddChildListProfileAdap"
    var imageTop: ImageView? = null
    override fun onBindViewHolder(holder: FreshDailyDealVH, position: Int) {

        Log.d(
            TAG,
            "onBindViewHolder: test>>100>" + arrayList[position].firstName + ">>" + arrayList[position].id + ">>" + arrayList[position].sportsIDS
        )

        if (arrayList[position].imageUri == null) {
            holder.imgProfile.setImageResource(R.mipmap.cam)
            Glide.with(context)
                .load(arrayList[position].imageUrl)
                .placeholder(R.mipmap.cam)
                .error(R.mipmap.cam)
                .into(holder.imgProfile)
        } else Glide.with(context)
            .load(arrayList[position].imageUri)
            .placeholder(R.mipmap.cam)
            .error(R.mipmap.cam)
            .into(holder.imgProfile)

        holder.txtChildName.text = arrayList[position].firstName+" "+arrayList[position].middleName+" "+arrayList[position].lastName
        holder.txtEmail.text = arrayList[position].email
        holder.txtBirthdate.text = arrayList[position].bdate
        holder.txtSports.text = arrayList[position].sports
        holder.txtMiddleName.text = arrayList[position].middleName
        holder.txtLastName.text = arrayList[position].lastName
        holder.txtJercyName.text = arrayList[position].jurseyName
        if (arrayList[position].gender == "m") {
            holder.imgMale.setImageResource(R.mipmap.rad)
            holder.imgFemale.setImageResource(R.mipmap.rad1)
        } else if (arrayList[position].gender == "f") {
            holder.imgMale.setImageResource(R.mipmap.rad1)
            holder.imgFemale.setImageResource(R.mipmap.rad)
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
        notifyItemInserted(arrayList.size - 1)
    }

    fun deleteItem(position: Int) {
        arrayList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class FreshDailyDealVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        private val txtEditChild: TextView = itemView.findViewById(R.id.txtEditChild)
        private val txtDelete: TextView = itemView.findViewById(R.id.txtDelete)
        val txtChildName: TextView = itemView.findViewById(R.id.txtChildName)
        val txtEmail: TextView = itemView.findViewById(R.id.txtEmail)
        val txtBirthdate: TextView = itemView.findViewById(R.id.txtBirthdate)
        val txtSports: TextView = itemView.findViewById(R.id.txtSports)
        val imgMale: ImageView = itemView.findViewById(R.id.imgMale)
        val imgFemale: ImageView = itemView.findViewById(R.id.imgFemale)
        val txtMale: TextView = itemView.findViewById(R.id.txtMale)
        val txtFemale: TextView = itemView.findViewById(R.id.txtFemale)

        val txtMiddleName: TextView = itemView.findViewById(R.id.txtChildMiddleName)
        val txtLastName: TextView = itemView.findViewById(R.id.txtChildLastName)
        val txtJercyName: TextView = itemView.findViewById(R.id.txtChildJercyName_profile)

        init {
            /*imgMale.setOnClickListener {
            }
            imgFemale.setOnClickListener {
            }
            txtMale.setOnClickListener {
            }
            txtFemale.setOnClickListener {
            }*/
            imgProfile.setOnClickListener {
                //imageTop = holder.imgProfile
                //addChildlistInterface!!.onImagePickSelected(position)
            }
            txtEditChild.setOnClickListener {
                addChildlistInterface!!.onEditChildSelected(
                    adapterPosition,
                    arrayList[adapterPosition]
                )
            }
            txtDelete.setOnClickListener {
                addChildlistInterface!!.onDeleteChildSelected(
                    adapterPosition,
                    arrayList[adapterPosition].id
                )
            }
        }
    }

    interface AddChildlistInterface {
        fun onImagePickSelected(position: Int)
        fun onEditChildSelected(position: Int, data: ChildListObject)
        fun onDeleteChildSelected(position: Int, id: String)
    }
}