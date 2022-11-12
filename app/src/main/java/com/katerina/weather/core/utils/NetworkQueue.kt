package com.katerina.weather.core.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import javax.inject.Inject

class NetworkQueue@Inject constructor(appContext: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(appContext.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}