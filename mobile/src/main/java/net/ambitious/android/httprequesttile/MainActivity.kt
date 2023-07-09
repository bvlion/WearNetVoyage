package net.ambitious.android.httprequesttile

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch
import net.ambitious.android.httprequesttile.compose.DummyAdCompose
import net.ambitious.android.httprequesttile.compose.ErrorDialogCompose
import net.ambitious.android.httprequesttile.compose.LoadingCompose
import net.ambitious.android.httprequesttile.compose.MenuBottomNavigation
import net.ambitious.android.httprequesttile.compose.MenuList
import net.ambitious.android.httprequesttile.compose.NativeAdCompose
import net.ambitious.android.httprequesttile.compose.RequestCreate
import net.ambitious.android.httprequesttile.compose.RequestHistoryDetailContent
import net.ambitious.android.httprequesttile.compose.RequestHistoryList
import net.ambitious.android.httprequesttile.compose.RulesDialogCompose
import net.ambitious.android.httprequesttile.compose.SavedRequestList
import net.ambitious.android.httprequesttile.data.Constant
import net.ambitious.android.httprequesttile.data.RequestParams
import net.ambitious.android.httprequesttile.data.RequestParams.Companion.parseRequestParams
import net.ambitious.android.httprequesttile.data.ResponseParams
import net.ambitious.android.httprequesttile.data.ResponseParams.Companion.parseResponseParams
import net.ambitious.android.httprequesttile.ui.theme.MainAnimatedVisibility
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MobileAds.initialize(this)
    viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    setContent {
      val errorDialog = viewModel.errorDialog.collectAsState()
      val scope = rememberCoroutineScope()
      val scaffoldState = rememberScaffoldState()

      val savedRequests = viewModel.savedRequest.collectAsState()
      val savedResponses = viewModel.savedResponse.collectAsState()
      val rules = viewModel.rules.collectAsState()
      val loading = viewModel.loading.collectAsState()

      ErrorDialogCompose(errorDialog.value) {
        viewModel.dismissErrorDialog()
      }

      RulesDialogCompose(rules.value) {
        viewModel.dismissRules()
      }

      BackHandler(viewModel.resultBottomSheet.isVisible) {
        scope.launch {
          viewModel.resultBottomSheet.hide()
        }
      }

      val bottomMenuIndex = remember { mutableStateOf(0) }
      val editRequest = remember { mutableStateOf<RequestParams?>(null) }
      val editRequestIndex = remember { mutableStateOf(-1) }
      val response = remember { mutableStateOf<ResponseParams?>(null) }


      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Scaffold(
            scaffoldState = scaffoldState,
            topBar = { NativeAdCompose() },
            content = {
              ModalBottomSheetLayout(
                sheetState = viewModel.resultBottomSheet,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetContent = {
                  response.value?.let { response ->
                    RequestHistoryDetailContent(response, it.calculateBottomPadding())
                  }
                }
              ) {
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
                    watchSync = { index, request ->
                      viewModel.saveRequest(null, null, index, request)
                    },
                    edit = { index, request ->
                      editRequestIndex.value = index
                      editRequest.value = request
                      bottomMenuIndex.value = 1
                    },
                    send = {
                      viewModel.sendRequest(scope, scaffoldState, it)
                    }
                  )
                  LoadingCompose(loading.value)
                }
                MainAnimatedVisibility(bottomMenuIndex.value == 1) {
                  RequestCreate(
                    editRequest.value?.title ?: "",
                    editRequest.value?.url ?: "https://",
                    editRequest.value?.method ?: Constant.HttpMethod.GET,
                    editRequest.value?.bodyType ?: Constant.BodyType.QUERY,
                    editRequest.value?.headers
                      ?: "Content-type:application/x-www-form-urlencoded\nUser-Agent:myApp\n",
                    editRequest.value?.parameters ?: "a=b",
                    editRequestIndex.value,
                    it.calculateBottomPadding(),
                    cancel = {
                      bottomMenuIndex.value = 0
                    },
                    save = { index, request ->
                      viewModel.saveRequest(scope, scaffoldState, index, request)
                      bottomMenuIndex.value = 0
                    },
                    delete = {
                      viewModel.deleteRequest(scope, scaffoldState, it)
                      bottomMenuIndex.value = 0
                    }
                  )
                }
                MainAnimatedVisibility(bottomMenuIndex.value == 2) {
                  RequestHistoryList(
                    if (savedResponses.value.isEmpty()) {
                      emptyList()
                    } else {
                      savedResponses.value.parseResponseParams()
                    },
                    it.calculateBottomPadding()
                  ) {
                    response.value = it
                    scope.launch { viewModel.resultBottomSheet.show() }
                  }
                }
                MainAnimatedVisibility(bottomMenuIndex.value == 3) {
                  MenuList(
                    it.calculateBottomPadding(),
                    historyDelete = {
                      viewModel.deleteResponses(scope, scaffoldState)
                    },
                    savePasteRequest = {
                      viewModel.saveRequest(scope, scaffoldState, it)
                    },
                    copyToClipboard = {
                      viewModel.copyToClipboard(clipboardManager, scope, scaffoldState, it)
                    },
                    showRules = {
                      viewModel.showRules(it)
                    }
                  )
                }
              }
                      },
            bottomBar = {
              MenuBottomNavigation(bottomMenuIndex) {
                if (it != 1) {
                  editRequest.value = null
                  editRequestIndex.value = -1
                }
                viewModel.hideBottomSheet(scope)
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