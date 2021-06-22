package com.example.mini_webbrowser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById<ContentLoadingProgressBar>(R.id.progressBar)
    }

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

    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
    }

    private val webView: WebView by lazy {
        findViewById<WebView>(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
        initButtons()
        refreshEvent()
    }



    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        webView.apply {
            webViewClient = MyWebViewClient()
            webChromeClient = MyWebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews() {
        addressText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = v.text.toString()

                if (URLUtil.isNetworkUrl(loadingUrl))
                    webView.loadUrl(loadingUrl)
                else
                    webView.loadUrl("http://${loadingUrl}")
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onBackPressed() {
        if(webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

    private fun initButtons() {
        backButton.setOnClickListener {
            webView.goBack()
        }

        forwardButton.setOnClickListener {
            webView.goForward()
        }

        homeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }
    }

    private fun refreshEvent() {
        refreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }


    inner class MyWebViewClient: WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressBar.hide()
            if (webView.canGoBack())
                backButton.isEnabled
            if (webView.canGoForward())
                forwardButton.isEnabled

            addressText.setText(url)

        }
    }

    inner class MyWebChromeClient: WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress
        }
    }

    companion object {
        private const val DEFAULT_URL = "https://www.google.com"
    }
}