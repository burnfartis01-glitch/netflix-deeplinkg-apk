package com.minimal.template.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          NetflixLinkTester()
        }
      }
    }
  }

  private fun openPlayStore(packageName: String) {
    val market = Uri.parse("market://details?id=$packageName")
    val web = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")

    try {
      startActivity(Intent(Intent.ACTION_VIEW, market))
    } catch (_: ActivityNotFoundException) {
      startActivity(Intent(Intent.ACTION_VIEW, web))
    }
  }

  private fun openNetflixTitle(titleId: String) {
    val deepLink = Uri.parse("nflx://www.netflix.com/title/$titleId")
    val intent = Intent(Intent.ACTION_VIEW, deepLink)

    try {
      startActivity(intent)
    } catch (_: ActivityNotFoundException) {
      // Netflix not installed (or deep link not handled)
      openPlayStore("com.netflix.mediaclient")
    }
  }

  @Composable
  private fun NetflixLinkTester() {
    var titleId by remember { mutableStateOf(TextFieldValue("60002820")) }
    var status by remember { mutableStateOf("Ready") }

    Column(modifier = Modifier.padding(16.dp)) {
      Text("Netflix deeplink test", style = MaterialTheme.typography.headlineSmall)
      Spacer(Modifier.height(12.dp))

      OutlinedTextField(
        value = titleId,
        onValueChange = { titleId = it },
        label = { Text("Netflix title ID") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(Modifier.height(12.dp))

      Button(
        onClick = {
          val id = titleId.text.trim()
          if (id.all { it.isDigit() } && id.isNotEmpty()) {
            status = "Opening Netflix (or Play Store)…"
            openNetflixTitle(id)
          } else {
            status = "Please enter a numeric title id (e.g. 60002820)"
          }
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("Open in Netflix (fallback to install)")
      }

      Spacer(Modifier.height(12.dp))
      Text("Status: $status")
    }
  }
}
