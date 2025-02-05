package com.xtrane.retrofit.controller

interface IBaseController {
    open fun onDestroy()
    open fun onFinish()
}