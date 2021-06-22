# 간단한 웹브라우저

### WebView
~~~kotlin
// 인터넷 권한 설정 in AdroidMenifest.xml
<uses-permission android:name="android.permission.INTERNET"/>
<application android:usesCleartextTraffic="true"> // http 허용
// in MainActivity.kt
// 기본적으로 안드로이드의 default 웹브라우저(chrome)로 실행 됨
// 현재 webView에서 보려면 webViewClient를 덮어씀
webView.webViewClient = WebViewClient()
webView.settings.javaScriptEnabled = true // webView에서 JS 동작
webView.loadUrl("url") // webView url 페이지 실행
~~~

### setOnEditorActionListener, IME_ACTION
IME_ACTION_DONE : 키보드 엔터버튼 입력 시 키보드가 내려감
~~~kotlin
// in activity_main.xml
<EditText
    android:imeOptions="actionDone" // 키보드 엔터버튼을 정의
    android:inputType="textUri"/> // 키보드 유형을 정의
// in MainActivity.kt
addressText.setOnEditorActionListener { v, actionId, event ->
	// IME_ACTION_DONE 일 경우 url 페이지 실행
    if (actionId == EditorInfo.IME_ACTION_DONE) {
        webView.loadUrl(v.text.toString())
    }
    return@setOnEditorActionListener false
~~~

### onBackPressed : 안드로이드 backButton 정의
~~~kotlin
// in MainActivity.kt
override fun onBackPressed() {
    if(webView.canGoBack()) // webView 히스토리가 남아있다면 종료X
        webView.goBack() // 뒤로가기
    else
        super.onBackPressed() // 프로세스 종료
    }
~~~

### Swiperefreshlayout : 화면을 아래로 드래그하여 새로고침
~~~kotlin
// in build.gradle
dependencies {
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

// in activity_main.xml // SwipeRefreshLayout 안에 WebView 정의
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
	android:id="@+id/refreshLayout"
	android:layout_width="0dp"
	android:layout_height="0dp"
	app:layout_constraintBottom_toBottomOf="parent"
	app:layout_constraintEnd_toEndOf="parent"
	app:layout_constraintStart_toStartOf="parent"
	app:layout_constraintTop_toBottomOf="@id/toolbar">
	<WebView
		android:id="@+id/webView"
		android:layout_width="match_parent"
		android:layout_height="match_parent" />
</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

// in MainActivity.kt
// 페이지 로딩이 완료되면 Refresh 버튼을 제거
inner class MyWebViewClient: WebViewClient() {
	override fun onPageFinished(view: WebView?, url: String?) {
		super.onPageFinished(view, url)

		refreshLayout.isRefreshing = false
	}
}
~~~

### ContentLoadingProgressBar
~~~kotlin
// in activity_main.xml
<androidx.core.widget.ContentLoadingProgressBar
	style="@style/Widget.AppCompat.ProgressBar.Horizontal"/>

// in MainActivity.kt
// WebView 의 프로그레스 변화를 progressBar 에 적용
inner class MyWebChromeClient: WebChromeClient() {
	override fun onProgressChanged(view: WebView?, newProgress: Int) {
		super.onProgressChanged(view, newProgress)

		progressBar.progress = newProgress
	}
}
~~~

### WebViewClient (기본기능)
~~~kotlin
// page loading을 시작했을 때 호출되는 콜백 메소드
onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
// page loading을 끝냈을 때 호출되는 콜백 메소드
onPageFinished(view: WebView?, url: String?)
~~~

### WebChromeClient (확장기능)
~~~kotlin
// page loading의 프로그레스를 호출
onProgressChanged(view: WebView?, newProgress: Int)
~~~

### 안드로이드 statusBar (color , textColor)
~~~kotlin
// in themes.xml
<item name="android:statusBarColor" tools:targetApi="l">@color/white</item>
<!-- Customize your theme here. -->
<item name="android:windowLightStatusBar">true</item> // 글자색 어둡게
~~~