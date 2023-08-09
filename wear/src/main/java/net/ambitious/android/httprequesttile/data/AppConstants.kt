package net.ambitious.android.httprequesttile.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.wear.widget.ConfirmationOverlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

object AppConstants {
  const val START_MOBILE_ACTIVITY = "net.ambitious.android.httprequesttile.START_MOBILE_ACTIVITY"
  const val SYNC_STORE_DATA = "net.ambitious.android.httprequesttile.SYNC_STORE_DATA"

  fun startMobileActivity(
    context: Context,
    scope: CoroutineScope
  ) {
    val remoteActivityHelper = RemoteActivityHelper(context)
    scope.launch {
      try {
        remoteActivityHelper.startRemoteActivity(
          Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse("httprequesttile://start"))
        ).await()
      } catch (e: Exception) {
        if (context is Activity) {
          ConfirmationOverlay()
            .setType(ConfirmationOverlay.FAILURE_ANIMATION)
            .showOn(context)
        } else {
          Toast.makeText(
            context,
            "スマートフォンのアプリがインストールされていません",
            Toast.LENGTH_LONG
          ).show()
        }
      }
    }
  }
}
