package net.ambitious.android.httprequesttile

import android.accounts.AccountManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.AccountPicker
import net.ambitious.android.httprequesttile.compose.DummyAdCompose
import net.ambitious.android.httprequesttile.compose.ErrorDialogCompose
import net.ambitious.android.httprequesttile.compose.MenuBottomNavigation
import net.ambitious.android.httprequesttile.compose.NativeAdCompose
import net.ambitious.android.httprequesttile.compose.RequestCreate
import net.ambitious.android.httprequesttile.compose.RequestHistoryList
import net.ambitious.android.httprequesttile.compose.SavedRequestList
import net.ambitious.android.httprequesttile.compose.SavedRequestListPreview
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme
import java.util.logging.Logger

class MainActivity : ComponentActivity() {

  private lateinit var viewModel: MainViewModel

  private val activityResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == RESULT_OK) {
        result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)?.let {
          viewModel.setUserAccount(it)
          return@registerForActivityResult
        }
      }
      viewModel.showErrorDialog()
    }

  private fun showChoose() {
    val intent = AccountPicker.newChooseAccountIntent(
      AccountPicker.AccountChooserOptions.Builder()
        .setTitleOverrideText("バックアップに利用するアカウントを選択して下さい")
        .build()
    )
    activityResult.launch(intent)
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MobileAds.initialize(this)
    viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    setContent {
      val errorDialog = viewModel.errorDialog.collectAsState()

      ErrorDialogCompose(errorDialog.value) {
        viewModel.dismissErrorDialog()
      }

      val bottomMenuIndex = remember { mutableStateOf(0) }

      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Scaffold(
            topBar = { NativeAdCompose() },
            content = {
              when (bottomMenuIndex.value) {
                0 -> SavedRequestList(it.calculateBottomPadding())
                1 -> RequestCreate(it.calculateBottomPadding())
                2 -> RequestHistoryList(it.calculateBottomPadding())
                else -> Log.e("MainActivity", "Unknown index: ${bottomMenuIndex.value}")
              }
                      },
            bottomBar = { MenuBottomNavigation(bottomMenuIndex) }
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  MyApplicationTheme {
    Scaffold(
      topBar = { DummyAdCompose() },
      content = { RequestCreate(it.calculateBottomPadding()) },
      bottomBar = { MenuBottomNavigation() }
    )
  }
}