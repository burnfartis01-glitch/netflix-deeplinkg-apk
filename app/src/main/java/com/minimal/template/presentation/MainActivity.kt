package com.minimal.template.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.app.Activity

class MainActivity : Activity() {

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
            openPlayStore("com.netflix.mediaclient")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.TOP
        }

        val title = TextView(this).apply {
            text = "Netflix deeplink test"
            textSize = 20f
        }

        val input = EditText(this).apply {
            hint = "Netflix title ID (e.g. 60002820)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText("60002820")
        }

        val status = TextView(this).apply {
            text = "Status: Ready"
        }

        val button = Button(this).apply {
            text = "Open in Netflix (fallback to install)"
            setOnClickListener {
                val id = input.text.toString().trim()
                if (id.isNotEmpty() && id.all { it.isDigit() }) {
                    status.text = "Status: Opening…"
                    openNetflixTitle(id)
                } else {
                    status.text = "Status: Enter a numeric title ID"
                }
            }
        }

        root.addView(title)
        root.addView(input)
        root.addView(button)
        root.addView(status)

        setContentView(root)
    }
}
