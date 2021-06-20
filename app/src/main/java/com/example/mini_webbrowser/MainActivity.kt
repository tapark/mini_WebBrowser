package com.example.mini_webbrowser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private val homeButton: ImageButton by lazy {
        findViewById<ImageButton>(R.id.homeButton)
    }

    private val backButton: ImageButton by lazy {
        findViewById<ImageButton>(R.id.backButton)
    }

    private val forwardButton: ImageButton by lazy {
        findViewById<ImageButton>(R.id.forwardButton)
    }

    private val addressText: EditText by lazy {
        findViewById<EditText>(R.id.addressBar)
    }

    private val webView: WebView by lazy {
        findViewById<WebView>(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }



    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("https://www.google.com")
        }
    }

    private fun bindViews() {
        addressText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                webView.loadUrl(v.text.toString())
            }

            return@setOnEditorActionListener false
        }
    }


}