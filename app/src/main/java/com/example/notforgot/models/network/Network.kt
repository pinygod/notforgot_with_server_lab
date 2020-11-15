package com.example.notforgot.models.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private var _instance: ApiInterface? = null

    fun getInstance(): ApiInterface {
        val instance = _instance

        if (instance != null) return instance

        val newInstance = Retrofit
            .Builder()
            .baseUrl("http://practice.mobile.kreosoft.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)

        _instance = newInstance
        return newInstance
    }
}