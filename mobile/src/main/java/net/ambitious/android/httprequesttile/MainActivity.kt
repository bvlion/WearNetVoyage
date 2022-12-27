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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.AccountPicker
import net.ambitious.android.httprequesttile.compose.ErrorDialogCompose
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme

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
    viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    setContent {
      val userAccount = viewModel.userAccount.collectAsState()
      val errorDialog = viewModel.errorDialog.collectAsState()

      ErrorDialogCompose(errorDialog.value) {
        viewModel.dismissErrorDialog()
      }

      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          if (userAccount.value == null && errorDialog.value == null) {
            showChoose()
          }
          Greeting("Android")
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  MyApplicationTheme {
    Greeting("Android")
  }
}