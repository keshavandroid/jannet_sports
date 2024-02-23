package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.nonparticipantdata.NonParticipanResult

interface INonParticipanView:IBaseView {

    fun onNonParticipantSuccess(response:List<NonParticipanResult?>?)
}