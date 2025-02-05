package com.xtrane.viewinterface

import com.xtrane.retrofit.alreadyjointeam.AlreadyJoinTeamResult

interface ICoachAlreadyJoinTeamView:IBaseView {

    fun alredyJoinTeamSuccess(resonse:List<AlreadyJoinTeamResult?>?)
}