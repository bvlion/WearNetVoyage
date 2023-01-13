package net.ambitious.android.httprequesttile.util

import org.json.JSONObject

object TestUtil {
  fun getArgsBody(body: String, key: String): String =
    JSONObject(body).getJSONObject("args").getString(key)

  fun getHeadersBody(body: String, key: String): String {
    val headers = JSONObject(body).getJSONObject("headers")
    headers.keys().forEach {
      val test = headers.get(it)
      val eq = it == key
      eq
      test
    }
    return headers.getString(key)
  }
}