package com.xtrane.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {

    private var retrofit: Retrofit? = null

    //    private val SERVER_URL = "https://keshavinfotechdemo2.com/keshav/KG2/Jannet_new/api/"
    //  private val SERVER_URL = "http://www.x-trane.com/api/"
    val SERVER_URL = "https://admin.x-trane.com/api/"


    fun getClient(): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("yyyy-MM-dd")
        gsonBuilder.disableHtmlEscaping()
        val client =
            OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS).build()

        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()

        return retrofit
    }
}