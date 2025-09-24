package com.xtrane.viewinterface

import com.xtrane.retrofit.nonparticipantdata.GetMemberResult

interface IGetTeamMemberView:IBaseView {

    fun onGetTeamMember(response:List<GetMemberResult.MemberResult?>?)
}