package com.e.jannet_stable_code.viewinterface

interface IBookTickitParentView:IBaseView {

    fun onBookTicketSuccessful(message:String)

    fun onBookTicketFailed()
}