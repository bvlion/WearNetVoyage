package net.ambitious.android.httprequesttile.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme
import net.ambitious.android.httprequesttile.ui.theme.noRippleClickable

@Composable
fun SavedRequest(addTopPadding: Dp = 0.dp, addBottomPadding: Dp = 0.dp) {
  val checked = remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp, 8.dp + addTopPadding, 8.dp, 8.dp + addBottomPadding),
    elevation = 2.dp,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.noRippleClickable { checked.value = !checked.value }.padding(end = 16.dp)
        ) {
          Checkbox(checked = checked.value, onCheckedChange = { checked.value = !checked.value })
          Text(text = "Watch に同期", fontSize = 12.sp)
        }
        Text(
          text = "HogeHoge の Request",
          modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 24.dp)
        )
      }
      IconButton(
        onClick = { },
      ) {
        Icon(
          Icons.Default.Send,
          contentDescription = "Localized description",
          modifier = Modifier.height(80.dp).width(80.dp).padding(24.dp)
        )
      }
    }

  }
}

@Composable
fun SavedRequestList(bottomPadding: Dp = 56.dp) {
  Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
    repeat(10) {
      SavedRequest(if (it == 0) 8.dp else 0.dp, if (it == 9) 8.dp + bottomPadding else 0.dp)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SavedRequestListPreview() {
  MyApplicationTheme {
    SavedRequestList()
  }
}
