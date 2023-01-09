package net.ambitious.android.httprequesttile.data

import com.google.android.gms.wearable.DataMap

data class RequestParams(
  val title: String,
  val url: String,
  val method: Constant.HttpMethod,
  val bodyType: Constant.BodyType,
  val headers: Map<String, String>?,
  val parameter: Map<String, String>?
) {
  fun toDataMap(): DataMap =
    DataMap().apply {
      putString(TITLE, title)
      putString(URL, url)
      putString(METHOD, method.toString())
      putString(BODY_TYPE, bodyType.toString())
      headers?.let {
        putDataMap(HEADERS, DataMap().apply {
          it.forEach { (k, v) -> putString(k, v) }
        })
      }
      parameter?.let {
        putDataMap(PARAMETER, DataMap().apply {
          it.forEach { (k, v) -> putString(k, v) }
        })
      }
    }

  fun toMap(): Map<String, Any> = HashMap<String, Any>().apply {
    put(TITLE, title)
    put(URL, url)
    put(METHOD, method)
    put(BODY_TYPE, bodyType)
    headers?.let {
      put(HEADERS, headers)
    }
    parameter?.let {
      put(PARAMETER, parameter)
    }
  }

  companion object {
    const val REQUEST_PARAMS_URI = "/request_params"
    const val REQUEST_PARAMS_LIST_KEY = "request_params_list"

    private const val TITLE = "title"
    private const val URL = "url"
    private const val METHOD = "method"
    private const val BODY_TYPE = "bodyType"
    private const val HEADERS = "headers"
    private const val PARAMETER = "parameter"
  }

  fun DataMap.toRequestParams(): RequestParams =
    RequestParams(
      getString(TITLE) ?: throw IllegalArgumentException("$TITLE is null"),
      getString(URL) ?: throw IllegalArgumentException("$URL is null"),
      getString(METHOD)?.let {
        Constant.HttpMethod.valueOf(it)
      } ?: throw IllegalArgumentException("$METHOD is null"),
      getString(BODY_TYPE)?.let {
        Constant.BodyType.valueOf(it)
      } ?: throw IllegalArgumentException("$BODY_TYPE is null"),
      getDataMap(HEADERS)?.run {
        HashMap<String, String>().apply {
          keySet().map {
            putString(it, getString(it) ?: throw IllegalArgumentException("$it is null"))
          }
        }
      },
      getDataMap(PARAMETER)?.run {
        HashMap<String, String>().apply {
          keySet().map {
            putString(it, getString(it) ?: throw IllegalArgumentException("$it is null"))
          }
        }
      }
    )
}