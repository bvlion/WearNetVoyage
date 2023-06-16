package net.ambitious.android.httprequesttile

import android.accounts.AccountManager
import android.os.Bundle
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
import net.ambitious.android.httprequesttile.compose.MenuList
import net.ambitious.android.httprequesttile.compose.NativeAdCompose
import net.ambitious.android.httprequesttile.compose.RequestCreate
import net.ambitious.android.httprequesttile.compose.RequestHistoryList
import net.ambitious.android.httprequesttile.compose.SavedRequestList
import net.ambitious.android.httprequesttile.ui.theme.MainAnimatedVisibility
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private lateinit var viewModel: MainViewModel

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
              MainAnimatedVisibility(bottomMenuIndex.value == 0) { SavedRequestList(it.calculateBottomPadding()) }
              MainAnimatedVisibility(bottomMenuIndex.value == 1) {
                RequestCreate(
                  it.calculateBottomPadding(),
                  cancel = { bottomMenuIndex.value = 0 },
                  save = {
                    viewModel::saveRequest
                    bottomMenuIndex.value = 0
                  }
                )
              }
              MainAnimatedVisibility(bottomMenuIndex.value == 2) { RequestHistoryList(it.calculateBottomPadding()) }
              MainAnimatedVisibility(bottomMenuIndex.value == 3) { MenuList(it.calculateBottomPadding()) }
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