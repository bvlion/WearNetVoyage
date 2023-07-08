package net.ambitious.android.httprequesttile.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ambitious.android.httprequesttile.data.AppDataStore
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme
import net.ambitious.android.httprequesttile.ui.theme.noRippleClickable

@Composable
fun MenuList(bottomPadding: Dp = 56.dp) {
  Column(
    Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Row(modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 16.dp)) {
      AppDataStore.ViewMode.values().forEach { mode ->
        RadioButton(
          selected = (mode == AppDataStore.ViewMode.DEFAULT),
          onClick = { }
        )
        Text(
          modifier = Modifier
            .padding(top = 12.dp)
            .noRippleClickable { },
          fontSize = 15.sp,
          style = MaterialTheme.typography.body1.merge(),
          text = when (mode) {
            AppDataStore.ViewMode.DEFAULT -> "デフォルト"
            AppDataStore.ViewMode.LIGHT -> "ライト"
            AppDataStore.ViewMode.DARK -> "ダーク"
          }
        )
      }
    }

    menus.forEach {
      Row(
        modifier = Modifier.fillMaxWidth().clickable { },
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          it.icon,
          contentDescription = it.dist,
          modifier = Modifier.padding(start = 16.dp)
        )
        Text(text = it.dist, modifier = Modifier.padding(16.dp))
      }
    }

    Box(modifier = Modifier.padding(bottom = bottomPadding))
  }
}

private val menus = listOf(
  MenuItem.SYNC_TO_WATCH,
  MenuItem.SYNC_TO_MOBILE,
  MenuItem.EXPORT,
  MenuItem.IMPORT,
  MenuItem.TERMS_OF_USE,
  MenuItem.PRIVACY_POLICY,
  MenuItem.REVIEW,
  MenuItem.OPINION
)

sealed class MenuItem(val dist: String, val icon: ImageVector, val onClick: () -> Unit = {}) {
  object SYNC_TO_WATCH : MenuItem("ウェアラブルへ Request を同期", Icons.Filled.Sync)
  object SYNC_TO_MOBILE : MenuItem("ウェアラブルの実行履歴を同期", Icons.Filled.Sync)
  object EXPORT : MenuItem("Request をエクスポート", Icons.Filled.Upload)
  object IMPORT : MenuItem("Request をインポート", Icons.Filled.Download)
  object TERMS_OF_USE : MenuItem("利用規約", Icons.Filled.Description)
  object PRIVACY_POLICY : MenuItem("プライバシー・ポリシー", Icons.Filled.Description)
  object REVIEW : MenuItem("レビューする", Icons.Filled.RateReview)
  object OPINION : MenuItem("ご意見", Icons.Filled.ContactMail)
}

@Preview(showBackground = true)
@Composable
fun MenuListPreview() {
  MyApplicationTheme {
    MenuList()
  }
}
