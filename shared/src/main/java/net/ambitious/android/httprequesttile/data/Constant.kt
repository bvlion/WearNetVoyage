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
}