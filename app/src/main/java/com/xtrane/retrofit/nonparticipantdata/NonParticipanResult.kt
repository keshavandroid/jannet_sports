package com.xtrane.retrofit.nonparticipantdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class NonParticipanResult {



    @SerializedName("userId")
    @Expose
    private var id: Int? = null

    @SerializedName("userName")
    @Expose
    private var name: String? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null


    @SerializedName("image")
    @Expose
    private var image: String? = null

    private var selected: Boolean?=false

    @SerializedName("isParticipant")
    @Expose
    private var isParticipant: Int? = null

    fun getParticipant(): Int?
    {
        return  isParticipant
    }
    fun getSelected():Boolean?{
        return  selected
    }

    fun setSelected(selected:Boolean?){

        this.selected = selected
    }
    

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }
}