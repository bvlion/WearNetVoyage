package net.ambitious.android.httprequesttile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.ambitious.android.httprequesttile.data.AppDataStore
import net.ambitious.android.httprequesttile.data.ErrorDetail
import net.ambitious.android.httprequesttile.data.RequestParams
import net.ambitious.android.httprequesttile.data.parseRequestParams
import org.json.JSONArray

class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val dataStore = AppDataStore.getDataStore(application)

  private val _savedRequest = MutableStateFlow<String?>("")
  val savedRequest = _savedRequest.asStateFlow()

  private val _errorDialog = MutableStateFlow<ErrorDetail?>(null)
  val errorDialog = _errorDialog.asStateFlow()

  init {
    viewModelScope.launch {
      dataStore.getSavedRequest.collect {
        _savedRequest.value = it
      }
    }
  }

  fun saveRequest(savedIndex: Int, request: RequestParams) {
    viewModelScope.launch {
      (savedRequest.value?.run {
        parseRequestParams()
          .toMutableList()
          .apply {
            if (savedIndex >= 0) {
              set(savedIndex, request)
            } else {
              add(0, request)
            }
          }
          .map { it.toJsonString() }
          .let { JSONArray(it).toString() }
      } ?: run {
        JSONArray(listOf(request.toJsonString())).toString()
      })
        .let { dataStore.saveRequest(it) }
    }
  }

  fun deleteRequest(deleteIndex: Int) {
    viewModelScope.launch {
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

  fun showErrorDialog() {
    _errorDialog.value = ErrorDetail("アカウントエラー", "当アプリではデータ保存のためのアカウントを選択していただく必要があります。")
  }

  fun dismissErrorDialog() {
    _errorDialog.value = null
  }
}