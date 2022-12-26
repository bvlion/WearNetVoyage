package net.ambitious.android.httprequesttile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(context: Context) {
  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
  private val settingsDataStore = context.dataStore

  val getUserAccount: Flow<String?> = settingsDataStore.data.map { pref ->
    pref[USER_ACCOUNT_KEY]
  }

  suspend fun setUserAccount(userAccount: String) = settingsDataStore.edit {
    it[USER_ACCOUNT_KEY] = userAccount
  }

  companion object {
    private val USER_ACCOUNT_KEY = stringPreferencesKey("user_account")

    private var dataStore: AppDataStore? = null
    fun getDataStore(context: Context) = dataStore ?: AppDataStore(context).also { dataStore = it }
  }
}