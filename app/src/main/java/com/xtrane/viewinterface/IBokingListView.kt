package com.xtrane.viewinterface

import com.xtrane.retrofit.bookinglistdata.BookingListResult

interface IBokingListView:IBaseView {

    fun onBookingDetailSuccess(response:List<BookingListResult?>?)
}