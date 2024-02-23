package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.bookinglistdata.BookingListResult

interface IBokingListView:IBaseView {

    fun onBookingDetailSuccess(response:List<BookingListResult?>?)
}