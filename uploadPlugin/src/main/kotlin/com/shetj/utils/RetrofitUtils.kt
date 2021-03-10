package com.shetj.utils

import com.shetj.uploadplugin.UploadApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtils {
    private var retrofit: Retrofit? = null
    val instance: Retrofit
        get() {
            if(retrofit == null) {
                val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                        message -> println(message) })
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
                val builder = OkHttpClient.Builder()
                builder.connectTimeout(15, TimeUnit.SECONDS)
                builder.readTimeout(20, TimeUnit.SECONDS)
                builder.writeTimeout(20, TimeUnit.SECONDS)
                builder.retryOnConnectionFailure(true)

                    .addInterceptor(httpLoggingInterceptor)
                val client = builder.build()
                retrofit = Retrofit.Builder()
                    .baseUrl("https://upload.pgyer.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            return retrofit!!
        }

}