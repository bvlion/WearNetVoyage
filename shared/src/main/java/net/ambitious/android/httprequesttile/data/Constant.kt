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
}