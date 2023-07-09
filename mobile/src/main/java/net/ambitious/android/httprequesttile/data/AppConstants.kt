package net.ambitious.android.httprequesttile.data

object AppConstants {
  const val TERMS_OF_USE_URL = ""
  const val PRIVACY_POLICY_URL = ""
  const val INQUIRY_URL = ""

  enum class ViewMode(val type: Int) {
    DEFAULT(0),
    LIGHT(1),
    DARK(2)
  }
}
