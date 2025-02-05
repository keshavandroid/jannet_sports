package com.xtrane.viewinterface

interface IBookTickitParentView:IBaseView {

    fun onBookTicketSuccessful(message:String)

    fun onBookTicketFailed()
}