package net.ambitious.android.httprequesttile

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.ambitious.android.httprequesttile.data.AppDataStore
import net.ambitious.android.httprequesttile.data.ErrorDetail
import net.ambitious.android.httprequesttile.data.RequestParams
import net.ambitious.android.httprequesttile.data.RequestParams.Companion.parseRequestParams
import net.ambitious.android.httprequesttile.data.ResponseParams
import net.ambitious.android.httprequesttile.data.ResponseParams.Companion.parseResponseParams
import net.ambitious.android.httprequesttile.request.HttpRequester
import org.json.JSONArray
import java.util.Date

@ExperimentalMaterialApi
class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val dataStore = AppDataStore.getDataStore(application)
  private val requester = HttpRequester()

  val resultBottomSheet = ModalBottomSheetState(ModalBottomSheetValue.Hidden, isSkipHalfExpanded = true)

  private val _savedRequest = MutableStateFlow<String?>("")
  val savedRequest = _savedRequest.asStateFlow()

  private val _savedResponse = MutableStateFlow("")
  val savedResponse = _savedResponse.asStateFlow()

  private val _errorDialog = MutableStateFlow<ErrorDetail?>(null)
  val errorDialog = _errorDialog.asStateFlow()

  private val _loading = MutableStateFlow(false)
  val loading = _loading.asStateFlow()

  init {
    viewModelScope.launch(Dispatchers.IO) {
      dataStore.getSavedRequest.collect {
        _savedRequest.value = it
      }
    }
    viewModelScope.launch(Dispatchers.IO) {
      dataStore.getSavedResponse.collect {
        _savedResponse.value = it
      }
    }
  }

  fun saveRequest(savedIndex: Int, request: RequestParams) {
    viewModelScope.launch(Dispatchers.IO) {
      (savedRequest.value?.parseRequestParams()?.toMutableList() ?: mutableListOf())
        .apply {
          if (savedIndex >= 0) {
            set(savedIndex, request)
          } else {
            add(0, request)
          }
        }
        .map { it.toJsonString() }
        .let { JSONArray(it).toString() }
        .let { dataStore.saveRequest(it) }
    }
  }

  fun deleteRequest(deleteIndex: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      savedRequest.value?.run {
        parseRequestParams()
          .toMutableList()
          .apply {
            removeAt(deleteIndex)
          }
          .map { it.toJsonString() }
          .let { JSONArray(it).toString() }
          .let { dataStore.saveRequest(it) }
      }
    }
  }

  fun sendRequest(request: RequestParams) {
    _loading.value = true
    val start = System.currentTimeMillis()
    viewModelScope.launch(Dispatchers.IO) {
      val response = try {
        requester.execute(request)
      } catch (e: Exception) {
        ResponseParams(
          request.title,
          -1,
          System.currentTimeMillis() - start,
          "",
          "通信エラーが発生しました。\n${e.message}",
          Date().time,
          true
        )
      }
      _loading.value = false

      if (savedResponse.value.isBlank()) {
        mutableListOf()
      } else {
        savedResponse.value.parseResponseParams().toMutableList()
      }
        .apply { add(response) }
        .sortedByDescending { it.sendDateTime }
        .map { it.toJsonString() }
        .let { JSONArray(it).toString() }
        .let { dataStore.saveResponse(it) }
    }
  }

  fun hideBottomSheet(scope: CoroutineScope) {
    scope.launch {
      resultBottomSheet.hide()
    }
  }

  fun dismissErrorDialog() {
    _errorDialog.value = null
  }
}