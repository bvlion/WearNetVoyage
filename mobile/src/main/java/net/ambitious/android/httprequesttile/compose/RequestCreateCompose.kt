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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.ambitious.android.httprequesttile.data.Constant
import net.ambitious.android.httprequesttile.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestCreate(bottomPadding: Dp = 56.dp) {

  val expanded = remember { mutableStateOf(false) }
  val selectedMethod = remember { mutableStateOf(Constant.HttpMethod.GET) }
  val selectedBodyType = remember { mutableStateOf(Constant.BodyType.FORM_PARAMS) }

  Column(
    Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())) {

    OutlinedTextField(
      value = "",
      onValueChange = {},
      label = { Text("タイトル") },
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
    )

    Row {
      ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
          expanded.value = !expanded.value
        },
        modifier = Modifier
          .width(160.dp)
          .padding(start = 16.dp, end = 8.dp)
      ) {
        TextField(
          readOnly = true,
          value = selectedMethod.value.name,
          onValueChange = { },
          label = { Text("Method") },
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
              expanded = expanded.value
            )
          },
          colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
          expanded = expanded.value,
          onDismissRequest = {
            expanded.value = false
          }
        ) {
          Constant.HttpMethod.values().forEach {
            DropdownMenuItem(onClick = {
              selectedMethod.value = it
              expanded.value = false
            }) {
              Text(text = it.name)
            }
          }
        }
      }

      ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
          expanded.value = !expanded.value
        },
        modifier = Modifier.padding(end = 16.dp, start = 8.dp)
      ) {
        TextField(
          readOnly = true,
          value = selectedBodyType.value.name,
          onValueChange = { },
          label = { Text("BodyType") },
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
              expanded = expanded.value
            )
          },
          colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
          expanded = expanded.value,
          onDismissRequest = {
            expanded.value = false
          }
        ) {
          Constant.BodyType.values().forEach {
            DropdownMenuItem(onClick = {
              selectedBodyType.value = it
              expanded.value = false
            }) {
              Text(text = it.name)
            }
          }
        }
      }
    }

    OutlinedTextField(
      value = "https://",
      onValueChange = {},
      label = { Text("URL") },
      modifier = Modifier
        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        .fillMaxWidth()
    )

    OutlinedTextField(
      value = "Content-type:application/x-www-form-urlencoded\nUser-Agent:ワイのアプリ\n",
      onValueChange = {},
      label = { Text("Request Headers") },
      singleLine = false,
      minLines = 7,
      modifier = Modifier
        .padding(16.dp, 8.dp)
        .fillMaxWidth()
    )

    OutlinedTextField(
      value = "a=b",
      onValueChange = {},
      label = { Text("Request Body") },
      singleLine = false,
      minLines = 7,
      modifier = Modifier
        .padding(16.dp, 8.dp)
        .fillMaxWidth()
    )

    Button(
      onClick = { /*TODO*/ },
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 8.dp).height(48.dp)
    ) {
      Text(text = "登録・更新")
    }

    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp + bottomPadding)
    ) {
      OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.weight(1f).padding(end = 8.dp).height(44.dp),
      ) {
        Icon(Icons.Filled.Close, contentDescription = "キャンセル")
        Text(text = "キャンセル", modifier = Modifier.padding(start = 8.dp))
      }

      OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.weight(1f).padding(start = 8.dp).height(44.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = MaterialTheme.colors.error
        )
      ) {
        Icon(Icons.Filled.Delete, contentDescription = "削除")
        Text(text = "削除", modifier = Modifier.padding(start = 8.dp))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun RequestCreatePreview() {
  MyApplicationTheme {
    RequestCreate()
  }
}
