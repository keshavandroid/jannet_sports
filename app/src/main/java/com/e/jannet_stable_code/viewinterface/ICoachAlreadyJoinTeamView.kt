package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.alreadyjointeam.AlreadyJoinTeamResult

interface ICoachAlreadyJoinTeamView:IBaseView {

    fun alredyJoinTeamSuccess(resonse:List<AlreadyJoinTeamResult?>?)
}