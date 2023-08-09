package net.ambitious.android.httprequesttile

import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.ambitious.android.httprequesttile.data.AppDataStore
import net.ambitious.android.httprequesttile.data.Constant

class MobilesDataListenerService : WearableListenerService() {

  private val dataStore by lazy { AppDataStore.getDataStore(this) }

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.IO + job)

  private val messageClient by lazy { Wearable.getMessageClient(application) }
  private val capabilityClient by lazy { Wearable.getCapabilityClient(application) }

  override fun onMessageReceived(messageEvent: MessageEvent) {
    super.onMessageReceived(messageEvent)
    when (messageEvent.path) {
      Constant.WEAR_SAVE_REQUEST_PATH ->
        scope.launch {
          dataStore.saveRequest(String(messageEvent.data))
          MainTileService.tileUpdate(this@MobilesDataListenerService)
        }
      Constant.WEAR_REQUEST_RESPONSE_PATH ->
        scope.launch {
          try {
            val nodes = capabilityClient
              .getCapability(Constant.MOBILE_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
              .await()
              .nodes

            nodes.map { node ->
              async {
                messageClient.sendMessage(
                  node.id,
                  Constant.MOBILE_SAVE_RESPONSE_PATH,
                  dataStore.getSavedResponse.first().toByteArray()
                ).await()
              }
            }.awaitAll()
          } catch (exception: Exception) {
            // TODO Firebase Crashlytics
          }
        }
      Constant.WEAR_SAVED_RESPONSE_PATH ->
        scope.launch {
          dataStore.saveResponse("")
        }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }
}