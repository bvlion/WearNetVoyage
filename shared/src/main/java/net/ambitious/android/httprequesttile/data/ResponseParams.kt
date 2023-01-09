package net.ambitious.android.httprequesttile.data

import com.google.android.gms.wearable.DataMap

data class ResponseParams(
  val title: String,
  val responseCode: Int,
  val execTime: Long,
  val header: String,
  val body: String
) {
  fun toDataMap(): DataMap =
    DataMap().apply {
      putString(TITLE, title)
      putInt(RESPONSE_CODE, responseCode)
      putLong(EXEC_TIME, execTime)
      putString(HEADER, header)
      putString(BODY, body)
    }

  companion object {
    const val RESPONSE_PARAMS_URI = "/response_params"
    const val RESPONSE_PARAMS_LIST_KEY = "response_params_list"

    private const val TITLE = "title"
    private const val RESPONSE_CODE = "responseCode"
    private const val EXEC_TIME = "execTime"
    private const val HEADER = "header"
    private const val BODY = "body"
  }

  fun DataMap.toResponseParams(): ResponseParams =
    ResponseParams(
      getString(TITLE) ?: throw IllegalArgumentException("$TITLE is null"),
      getInt(RESPONSE_CODE),
      getLong(EXEC_TIME),
      getString(HEADER) ?: throw IllegalArgumentException("$HEADER is null"),
      getString(BODY) ?: throw IllegalArgumentException("$BODY is null"),
    )
}