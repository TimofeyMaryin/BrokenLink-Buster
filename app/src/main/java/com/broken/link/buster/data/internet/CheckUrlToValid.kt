package com.broken.link.buster.data.internet

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import kotlin.coroutines.resume

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @GET
    fun checkUrl(@Url url: String): Call<Void>
}

suspend fun connectInternetConnection(url: String): Boolean {
    return withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val api = RetrofitInstance.api
            val call = api.checkUrl(url)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 404) {
                        Log.e("TAG--UTILS", "checkUrlAvailable: 404 error")
                        continuation.resume(false)
                    } else {
                        Log.e("TAG--UTILS", "checkUrlAvailable: success")
                        continuation.resume(true)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("TAG--UTILS", "checkUrlAvailable: onFailure error")
                    continuation.resume(false)
                }
            })

            continuation.invokeOnCancellation {
                call.cancel()
            }
        }
    }

}