package net.ambitious.android.httprequesttile

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.ambitious.android.httprequesttile.data.AppConstants
import net.ambitious.android.httprequesttile.data.AppDataStore
import net.ambitious.android.httprequesttile.data.RequestParams.Companion.parseRequestParams
import net.ambitious.android.httprequesttile.tile.LinkTileRenderer
import net.ambitious.android.httprequesttile.tile.LinkTileState

@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService() {
  private val dataStore by lazy { AppDataStore.getDataStore(this) }

  private val savedRequest = MutableStateFlow<String?>("")

  override fun onCreate() {
    super.onCreate()
    lifecycleScope.launch(Dispatchers.IO) {
      dataStore.getSavedRequest.collect {
        savedRequest.value = it
      }
    }
  }

  override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources =
    render.produceRequestedResources(true, requestParams)

  override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
    if (requestParams.state?.lastClickableId == AppConstants.START_MOBILE_ACTIVITY) {
      AppConstants.startMobileActivity(this, lifecycleScope)
    }
    return render.renderTimeline(
      LinkTileState(
        savedRequest.value?.let {
          if (it.isEmpty()) {
            null
          } else {
            it.parseRequestParams()
          }
        } ?: listOf()
      ),
      requestParams
    )
  }

  private val render by lazy { LinkTileRenderer(this) }

  companion object {
    fun tileUpdate(context: Context) {
      getUpdater(context).requestUpdate(MainTileService::class.java)
    }
  }
}