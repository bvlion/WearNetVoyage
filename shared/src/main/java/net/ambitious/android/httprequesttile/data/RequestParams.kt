package net.ambitious.android.httprequesttile.data

import org.json.JSONArray
import org.json.JSONObject

data class RequestParams(
  val title: String,
  val url: String,
  val method: Constant.HttpMethod,
  val bodyType: Constant.BodyType,
  val headers: String = "",
  val parameters: String = "",
  val watchSync: Boolean = false
) {
  fun toJsonString(): String = JSONObject().apply {
    put(TITLE, title)
    put(URL, url)
    put(METHOD, method.name)
    put(BODY_TYPE, bodyType.name)
    put(HEADERS, headers)
    put(PARAMETERS, parameters)
    put(WATCH_SYNC, watchSync)
  }.toString()

  companion object {
    const val REQUEST_PARAMS_URI = "/request_params"
    const val REQUEST_PARAMS_LIST_KEY = "request_params_list"

    const val TITLE = "title"
    const val URL = "url"
    const val METHOD = "method"
    const val BODY_TYPE = "bodyType"
    const val HEADERS = "headers"
    const val PARAMETERS = "parameters"
    const val WATCH_SYNC = "watchSync"
  }
}

fun String.parseRequestParams(): List<RequestParams> {
  val list = mutableListOf<RequestParams>()
  val jsonArray = JSONArray(this)
  for (i in 0 until jsonArray.length()) {
    JSONObject(jsonArray[i].toString()).let {
      RequestParams(
        it.getString(RequestParams.TITLE),
        it.getString(RequestParams.URL),
        Constant.HttpMethod.valueOf(it.getString(RequestParams.METHOD)),
        Constant.BodyType.valueOf(it.getString(RequestParams.BODY_TYPE)),
        it.getString(RequestParams.HEADERS),
        it.getString(RequestParams.PARAMETERS),
        it.getBoolean(RequestParams.WATCH_SYNC)
      )
    }.let(list::add)
  }
  return list
}
