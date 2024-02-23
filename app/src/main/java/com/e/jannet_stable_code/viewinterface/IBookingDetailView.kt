package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.bookingdetaildata.BookingDetailResult

interface IBookingDetailView:IBaseView {

    fun onFBookingDetailSuccess(response:List<BookingDetailResult?>?)
}