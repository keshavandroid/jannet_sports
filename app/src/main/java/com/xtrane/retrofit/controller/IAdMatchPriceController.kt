package com.xtrane.retrofit.controller

import java.util.ArrayList

interface IAdMatchPriceController:IBaseController {


    fun callAddMatchPriceApi(id:String, token:String, matchId1: ArrayList<String>, matchPrice1:ArrayList<String>, count:String)
}



