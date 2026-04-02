package com.streamcast.tvbrowserkotlin

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    private var currentUrl = "https://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Main container
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF121212.toInt())
        }

        // URL Bar
        urlInput = EditText(this).apply {
            hint = "Enter URL..."
            setText(currentUrl)
            setTextColor(0xFFFFFFFF.toInt())
            setHintTextColor(0xFF888888.toInt())
            textSize = 18f
            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    loadUrl(urlInput.text.toString())
                    true
                } else false
            }
        }
        container.addView(urlInput)

        // Progress bar
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                8
            )
        }
        container.addView(progressBar)

        // Status text
        statusText = TextView(this).apply {
            text = "Ready"
            setTextColor(0xFFB3B3B3.toInt())
            textSize = 14f
            setPadding(16, 8, 16, 8)
        }
        container.addView(statusText)

        // WebView
        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    urlInput.setText(url)
                    statusText.text = "Loaded: ${url}"
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.progress = newProgress
                    if (newProgress == 100) {
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    statusText.text = title ?: ""
                }
            }
        }
        container.addView(webView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        ))

        setContentView(container)
        loadUrl(currentUrl)
    }

    private fun loadUrl(url: String) {
        currentUrl = if (url.startsWith("http")) url else "https://$url"
        webView.loadUrl(currentUrl)
        statusText.text = "Loading: $currentUrl"
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                webView.scrollBy(-50, 0)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                webView.scrollBy(50, 0)
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                webView.scrollBy(0, -50)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                webView.scrollBy(0, 50)
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                // Just request focus for now
                webView.requestFocus()
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
