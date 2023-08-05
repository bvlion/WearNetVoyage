package net.ambitious.android.httprequesttile.data

object Constant {
  enum class BodyType {
    FORM_PARAMS,
    JSON,
    QUERY
  }

  enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE;
  }

  const val WEAR_CAPABILITY = "wear"
  const val MOBILE_CAPABILITY = "mobile"

  const val WEAR_SAVE_REQUEST_PATH = "/save-request-params"
  const val WEAR_REQUEST_RESPONSE_PATH = "/request-to-send-responses"
  const val WEAR_SAVED_RESPONSE_PATH = "/saved-send-responses"

  const val MOBILE_SAVE_RESPONSE_PATH = "/save-wear-responses"
}