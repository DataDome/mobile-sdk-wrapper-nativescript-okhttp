package co.datadome.ns.wrapper

import android.app.Application
import android.util.Log
import co.datadome.sdk.DataDomeInterceptor
import co.datadome.sdk.DataDomeSDK
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class DataDomeSDKWrapper(
    application: Application,
    dataDomeSDKKey: String,
    appVersion: String,
    private val enableLogs: Boolean
) {

    interface CompletionHandler {
        fun onSuccess(response: String)
        fun onError(error: String)
    }

    companion object {
        private const val TAG = "DataDomeSDKWrapper"
    }

    private val dataDomeSDK =
        DataDomeSDK.with(application, dataDomeSDKKey, appVersion).activateDatadomeLogger(enableLogs)
    private val okHttpClient: OkHttpClient

    init {
        Log.d(TAG, "init DataDomeSDKWrapper")
        val dataDomeInterceptor = DataDomeInterceptor(application, dataDomeSDK)
        okHttpClient = OkHttpClient.Builder().addInterceptor(dataDomeInterceptor).build()
    }

    fun clearDataDomeCookie() {
        @Suppress("DEPRECATION") dataDomeSDK.clearDataDomeCookie()
    }

    fun makeRequest(
        method: String,
        url: String,
        headers: Map<String, String>? = null,
        body: String? = null,
        handler: CompletionHandler
    ) {
        val requestBuilder = Request.Builder().url(url)

        headers?.forEach { (key, value) -> requestBuilder.addHeader(key, value) }

        when (method.lowercase()) {
            "get" -> requestBuilder.get()
            "post" -> requestBuilder.post(body?.toRequestBody() ?: "".toRequestBody())
            "put" -> requestBuilder.put(body?.toRequestBody() ?: "".toRequestBody())
            "delete" -> requestBuilder.delete(body?.toRequestBody() ?: "".toRequestBody())
            "patch" -> requestBuilder.patch(body?.toRequestBody() ?: "".toRequestBody())
            else -> throw IllegalArgumentException("Invalid HTTP method: $method")
        }

        val request = requestBuilder.build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (enableLogs == true) {
                    Log.e(TAG, "HTTP request failed", e)
                }
                handler.onError(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string() ?: ""
                val jsonResponse = JSONObject().apply {
                    put("statusCode", response.code)
                    put("headers", response.headers.toMultimap())
                    put("content", bodyString)
                }.toString()

                if (enableLogs) {
                    Log.e(TAG, "HTTP response: $jsonResponse")
                }
                handler.onSuccess(jsonResponse)
            }

        })
    }

}