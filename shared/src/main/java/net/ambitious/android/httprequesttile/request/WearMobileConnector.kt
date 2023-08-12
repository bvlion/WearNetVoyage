package net.ambitious.android.httprequesttile.request

import android.content.Context
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class WearMobileConnector(context: Context) {

  private val messageClient by lazy { Wearable.getMessageClient(context) }
  private val capabilityClient by lazy { Wearable.getCapabilityClient(context) }

  suspend fun sendMessageToMobile(path: String, data: ByteArray = byteArrayOf()) =
    sendMessage(MOBILE_CAPABILITY, path, data)

  suspend fun sendMessageToWear(path: String, data: ByteArray = byteArrayOf()) =
    sendMessage(WEAR_CAPABILITY, path, data)

  private suspend fun sendMessage(
    capability: String, path: String, data: ByteArray
  ) = coroutineScope {
    try {
      val nodes = capabilityClient
        .getCapability(capability, CapabilityClient.FILTER_REACHABLE)
        .await()
        .nodes

      nodes.map { node ->
        async {
          messageClient.sendMessage(node.id, path, data).await()
        }
      }.awaitAll()
    } catch (exception: Exception) {
      // TODO Firebase Crashlytics
    }
  }

  companion object {
    private const val WEAR_CAPABILITY = "wear"
    private const val MOBILE_CAPABILITY = "mobile"

    const val WEAR_SAVE_REQUEST_PATH = "/save-request-params"
    const val WEAR_REQUEST_RESPONSE_PATH = "/request-to-send-responses"
    const val WEAR_SAVED_RESPONSE_PATH = "/saved-send-responses"

    const val MOBILE_SAVE_RESPONSE_PATH = "/save-wear-responses"
  }
}