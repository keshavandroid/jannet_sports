package com.xtrane.viewinterface

import com.xtrane.retrofit.bookingdetaildata.BookingDetailResult

interface IBookingDetailView:IBaseView {

    fun onFBookingDetailSuccess(response:List<BookingDetailResult?>?)
}