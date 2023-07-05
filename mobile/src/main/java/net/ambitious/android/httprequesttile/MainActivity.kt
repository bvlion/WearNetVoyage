package net.ambitious.android.httprequesttile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import net.ambitious.android.httprequesttile.compose.DummyAdCompose
import net.ambitious.android.httprequesttile.compose.ErrorDialogCompose
import net.ambitious.android.httprequesttile.compose.MenuBottomNavigation
import net.ambitious.android.httprequesttile.compose.MenuList
import net.ambitious.android.httprequesttile.compose.NativeAdCompose
import net.ambitious.android.httprequesttile.compose.RequestCreate
import net.ambitious.android.httprequesttile.compose.RequestHistoryList
import net.ambitious.android.httprequesttile.compose.SavedRequestList
import net.ambitious.android.httprequesttile.data.Constant
import net.ambitious.android.httprequesttile.data.RequestParams
import net.ambitious.android.httprequesttile.data.parseRequestParams
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
      val savedRequests = viewModel.savedRequest.collectAsState()

      ErrorDialogCompose(errorDialog.value) {
        viewModel.dismissErrorDialog()
      }

      val bottomMenuIndex = remember { mutableStateOf(0) }
      val editRequest = remember { mutableStateOf<RequestParams?>(null) }
      val editRequestIndex = remember { mutableStateOf(-1) }

      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Scaffold(
            topBar = { NativeAdCompose() },
            content = {
              MainAnimatedVisibility(bottomMenuIndex.value == 0) {
                SavedRequestList(
                  requests = if (savedRequests.value == null) {
                    emptyList()
                  } else {
                    savedRequests.value?.let {
                      if (it.isEmpty()) {
                        null // 起動時に入力画面が出ないようにする
                      } else {
                        it.parseRequestParams()
                      }
                    }
                  },
                  newCreateClick = { bottomMenuIndex.value = 1 },
                  bottomPadding = it.calculateBottomPadding(),
                  edit = { index, request ->
                    editRequestIndex.value = index
                    editRequest.value = request
                    bottomMenuIndex.value = 1
                  },
                )
              }
              MainAnimatedVisibility(bottomMenuIndex.value == 1) {
                RequestCreate(
                  editRequest.value?.title ?: "",
                  editRequest.value?.url ?: "https://",
                  editRequest.value?.method ?: Constant.HttpMethod.GET,
                  editRequest.value?.bodyType ?: Constant.BodyType.QUERY,
                  editRequest.value?.headers ?: "Content-type:application/x-www-form-urlencoded\nUser-Agent:ワイのアプリ\n",
                  editRequest.value?.parameters ?: "a=b",
                  editRequestIndex.value,
                  it.calculateBottomPadding(),
                  cancel = {
                    bottomMenuIndex.value = 0
                           },
                  save = { index, request ->
                    viewModel.saveRequest(index, request)
                    bottomMenuIndex.value = 0
                  },
                  delete = {
                    viewModel.deleteRequest(it)
                    bottomMenuIndex.value = 0
                  }
                )
              }
              MainAnimatedVisibility(bottomMenuIndex.value == 2) { RequestHistoryList(it.calculateBottomPadding()) }
              MainAnimatedVisibility(bottomMenuIndex.value == 3) { MenuList(it.calculateBottomPadding()) }
                      },
            bottomBar = {
              MenuBottomNavigation(bottomMenuIndex) {
                if (it != 1) {
                  editRequest.value = null
                  editRequestIndex.value = -1
                }
              }
            }
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
      content = { SavedRequestList(emptyList(), bottomPadding = it.calculateBottomPadding()) },
      bottomBar = { MenuBottomNavigation() }
    )
  }
}