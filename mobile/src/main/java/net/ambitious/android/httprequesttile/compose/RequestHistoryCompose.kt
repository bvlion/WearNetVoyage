package net.ambitious.android.httprequesttile.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme

@Composable
fun RequestHistory(addTopPadding: Dp = 0.dp, addBottomPadding: Dp = 0.dp) {
  val dialogShown = remember { mutableStateOf(false) }
  RequestHistoryDetailDialog(dialogShown)

  Card(
    modifier = Modifier
      .clickable { dialogShown.value = true }
      .fillMaxWidth()
      .padding(8.dp, 8.dp + addTopPadding, 8.dp, 8.dp + addBottomPadding),
    elevation = 2.dp,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "HogeHoge の Request",
        modifier = Modifier.padding(start = 16.dp)
      )
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = "Response: 200",
          fontSize = 14.sp
        )
        Text(
          text = "2023/05/23 10:33:45",
          modifier = Modifier.padding(top = 8.dp),
          fontSize = 12.sp
        )
      }
    }
  }
}

@Composable
fun RequestHistoryList(bottomPadding: Dp = 56.dp) {
  Column(
    Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())) {
    repeat(10) {
      RequestHistory(if (it == 0) 8.dp else 0.dp, if (it == 9) 8.dp + bottomPadding else 0.dp)
    }
  }
}

@Composable
fun RequestHistoryDetailDialog(dialogShown: MutableState<Boolean>) {
  if (dialogShown.value) {
    AlertDialog(
      onDismissRequest = { dialogShown.value = false },
      title = {
        Column {
          Text(
            text = "2023/05/23 10:33:45",
            modifier = Modifier.padding(end = 8.dp),
            fontSize = 14.sp
          )
          Text("HogeHoge の Request")
        }
              },
      text = {
        Column(modifier = Modifier.padding(8.dp)) {
          Row {
            Text(
              text = "Response Code:",
              modifier = Modifier.padding(end = 8.dp),
              fontSize = 14.sp
            )
            Text("200")
          }
          Text(
            text = "Response Body:",
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 14.sp
          )
          Text(
            text = "ほげほげ",
            modifier = Modifier.padding(top = 8.dp),
          )
          Text(
            text = "Response Header:",
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 12.sp
          )
          Text(
            text = "ほげほげ",
            modifier = Modifier.padding(top = 8.dp),
          )
        }
             },
      confirmButton = {
        TextButton(onClick = { dialogShown.value = false }) { Text("閉じる") }
      }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun RequestHistoryListPreview() {
  MyApplicationTheme {
    RequestHistoryList()
  }
}
