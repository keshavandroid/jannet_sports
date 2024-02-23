package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.bookinglistdata.BookTicketDetailResult

interface IBookTicketDetailView : IBaseView {

    fun onBookTicketDetailSuccessful(response: List<BookTicketDetailResult?>?)

}
