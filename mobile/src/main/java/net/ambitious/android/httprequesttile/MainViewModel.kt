package net.ambitious.android.httprequesttile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.ambitious.android.httprequesttile.data.AppDataStore

class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val dataStore = AppDataStore.getDataStore(application)
}