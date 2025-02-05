package com.xtrane.viewinterface

import com.xtrane.retrofit.bookinglistdata.BookTicketDetailResult

interface IBookTicketDetailView : IBaseView {

    fun onBookTicketDetailSuccessful(response: List<BookTicketDetailResult?>?)

}
