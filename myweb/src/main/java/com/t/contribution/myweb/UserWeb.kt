package com.t.contribution.myweb

import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.text.Charsets.UTF_8


@Composable
fun UserWebView() {

    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                allowFileAccess = true
                allowContentAccess = true
                javaScriptCanOpenWindowsAutomatically = true
                userAgentString = userAgents.random()
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = MyWebViewClient()
            loadUrl(decrypt("0910071618504b5c11031a011e0404050f184d040f4c0830572b272f"))
        }
    }
    CookieManager.getInstance().setAcceptCookie(true)
    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

    AndroidView(
        factory = { webView },
        update = {
            it.loadUrl(decrypt("0910071618504b5c11031a011e0404050f184d040f4c0830572b272f"))
        },
        modifier = Modifier.fillMaxSize()
    )
}

internal class MyWebViewClient() : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url!!)
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }

}




internal val userAgents = listOf(
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/96.0.1",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/96.0.1",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Safari/537.36 Edg/96.0.1054.49",
    "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36",
    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:95.0) Gecko/20100101 Firefox/95.0",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.2 Safari/605.1.15"
)

const val SECRETE_KEY = "adsfkjdsafhdsajkfscvzcxvfs"
fun encrypt(text: String, key: String = SECRETE_KEY): String {
    val textBytes = text.toByteArray(UTF_8)
    val keyBytes = key.toByteArray(UTF_8)

    val encryptedBytes = ByteArray(textBytes.size)

    for (i in textBytes.indices) {
        encryptedBytes[i] = (textBytes[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
    }

    return encryptedBytes.joinToString("") { byte -> byte.toInt().and(0xFF).toString(16).padStart(2, '0') }
}

fun decrypt(encryptedText: String, key: String = SECRETE_KEY): String {
    val encryptedBytes = encryptedText.chunked(2)
        .mapNotNull { it.takeIf { it.all { c -> c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F' } }?.toInt(16)?.toByte() }
        .toByteArray()
    val keyBytes = key.toByteArray(UTF_8)

    val decryptedBytes = ByteArray(encryptedBytes.size)

    for (i in encryptedBytes.indices) {
        decryptedBytes[i] = (encryptedBytes[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
    }

    return String(decryptedBytes, UTF_8)
}