package com.example.notforgot.models.network

import android.content.Context
import com.example.notforgot.utils.PreferenceUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private var _instance: ApiInterface? = null

    fun getInstance(context: Context): ApiInterface {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header(
                        "Authorization", "Bearer " + PreferenceUtils.getUserToken(
                            context
                        )
                    )
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()


        val instance = _instance

        if (instance != null) return instance

        val newInstance = Retrofit
            .Builder()
            .baseUrl("http://practice.mobile.kreosoft.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(ApiInterface::class.java)

        _instance = newInstance
        return newInstance
    }
}