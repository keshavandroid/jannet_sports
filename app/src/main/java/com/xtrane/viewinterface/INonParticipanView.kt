package com.xtrane.viewinterface

import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult

interface INonParticipanView:IBaseView {

    fun onNonParticipantSuccess(response:List<NonParticipanResult?>?)
}