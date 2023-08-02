package net.ambitious.android.httprequesttile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.wear.widget.ConfirmationOverlay
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import net.ambitious.android.httprequesttile.theme.HttpRequestTileTheme

class MainActivity : ComponentActivity() {

    private val remoteActivityHelper by lazy { RemoteActivityHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(::startMobileActivity)
        }
    }

    private fun startMobileActivity() {
        lifecycleScope.launch {
            try {
                remoteActivityHelper.startRemoteActivity(
                    Intent(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse("httprequesttile://start"))
                ).await()
            } catch (e: Exception) {
                ConfirmationOverlay()
                    .setType(ConfirmationOverlay.FAILURE_ANIMATION)
                    .showOn(this@MainActivity)
            }
        }
    }
}

@Composable
fun WearApp(startMobileActivity: () -> Unit = {}) {
    HttpRequestTileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = startMobileActivity,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Text(
                    "スマートフォンを\n起動する",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}