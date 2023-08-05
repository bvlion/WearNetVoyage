package net.ambitious.android.httprequesttile

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.ambitious.android.httprequesttile.data.AppDataStore
import net.ambitious.android.httprequesttile.data.Constant

class MobilesDataListenerService : WearableListenerService() {

  private val dataStore by lazy { AppDataStore.getDataStore(this) }

  private val job = SupervisorJob()
  private val scope = CoroutineScope(Dispatchers.IO + job)

  override fun onMessageReceived(messageEvent: MessageEvent) {
    super.onMessageReceived(messageEvent)
    when (messageEvent.path) {
      Constant.WEAR_SAVE_REQUEST_PATH ->
        scope.launch {
          dataStore.saveRequest(String(messageEvent.data))
        }
    }
  }
}