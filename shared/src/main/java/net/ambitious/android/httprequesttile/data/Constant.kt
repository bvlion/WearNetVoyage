package net.ambitious.android.httprequesttile.data

object Constant {
  enum class BodyType {
    FORM_PARAMS,
    JSON,
    QUERY
  }

  enum class HttpMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
  }
}