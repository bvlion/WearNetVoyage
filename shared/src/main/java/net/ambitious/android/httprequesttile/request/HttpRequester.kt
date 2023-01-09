package net.ambitious.android.httprequesttile.request

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ambitious.android.httprequesttile.data.Constant
import net.ambitious.android.httprequesttile.data.RequestParams
import net.ambitious.android.httprequesttile.data.ResponseParams
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.HttpMethod
import org.json.JSONObject
import java.net.URLEncoder

class HttpRequester {
  private val client = OkHttpClient()

  suspend fun execute(params: RequestParams): ResponseParams = withContext(Dispatchers.IO) {
    val start = System.currentTimeMillis()

    val body = when (params.bodyType) {
      Constant.BodyType.JSON -> params.parameter?.let {
        val json = JSONObject()
        it.forEach { (k, v) ->
          json.put(k, v)
        }
        json.toString()
      }
      else -> params.parameter?.map { (k, v) ->
        "${URLEncoder.encode(k, PARAMETER_ENCODING)}=${URLEncoder.encode(v, PARAMETER_ENCODING)}"
      }?.joinToString("&")
    }

    val request = Request.Builder()
      .url(
        if (HttpMethod.permitsRequestBody(params.method.toString()) || body == null) {
          params.url
        } else {
          "${params.url}?$body"
        }
      )
      .method(
        params.method.toString(),
        if (HttpMethod.permitsRequestBody(params.method.toString())) {
          body?.toByteArray()?.let {
            it.toRequestBody(
              if (params.bodyType == Constant.BodyType.JSON) {
                "application/json"
              } else {
                "application/x-www-form-urlencoded"
              }.toMediaType(),
              0,
              it.size
            )
          }
        } else null
      )
      .apply {
        params.headers?.let {
          headers(it.toHeaders())
        }
      }
      .build()
    client.newCall(request).execute().use { res ->
      val headerJson = JSONObject()
      res.headers.forEach {
        headerJson.put(it.first, it.second)
      }

      return@withContext ResponseParams(
        params.title,
        res.code,
        System.currentTimeMillis() - start,
        headerJson.toString(),
        res.body?.string() ?: ""
      )
    }
  }

  companion object {
    private const val PARAMETER_ENCODING = "UTF-8"
  }
}