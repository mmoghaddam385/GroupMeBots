package com.moghies.gmbot

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.moghies.gmbot.task.net.BotIndexTask

class GroupMeAuthActivity : AppCompatActivity() {

    lateinit private var webView: WebView

    companion object {
        private const val OAUTH_URL = "https://oauth.groupme.com/oauth/authorize?client_id=ds11ZmB6TbXrds3Zxv38wFRSLNPzyfv9GH1B5YYqL02pUbfJ"
        private const val ACCESS_TOKEN_PARAM = "access_token="
        private const val OAUTH_CALLBACK = "localhost/oauth_callback?$ACCESS_TOKEN_PARAM"

        const val BOTS_RECEIVED_EXTRA = "bots"
        const val BOTS_RECEIVED_RESULT = 1000
        const val ERROR_RECEIVING_BOTS_RESULT = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setTitle(R.string.sign_in)
        val progressDialog = ProgressDialog.show(this, null, "Loading login page...")
        CookieManager.getInstance().removeAllCookie();

        webView = WebView(this)

        setContentView(webView)

        webView.setWebViewClient(GroupMeAuthWebViewClient(progressDialog, this::onAuth))
        webView.loadUrl(OAUTH_URL)
    }

    private fun onAuth(token: String) {
        webView.visibility = View.INVISIBLE

        val progressDialog = ProgressDialog.show(this, null, "Loading bots...")

        BotIndexTask(token, {bots ->
            progressDialog.dismiss()

            if (bots != null) {
                val intent = Intent().putExtra(BOTS_RECEIVED_EXTRA, bots.toTypedArray())
                setResult(BOTS_RECEIVED_RESULT, intent)
            } else {
                setResult(ERROR_RECEIVING_BOTS_RESULT)
            }

            finish()
        }).execute()
    }

    inner class GroupMeAuthWebViewClient(val progressDialog: ProgressDialog, val onAuthCallback: (token: String) -> Unit) : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            progressDialog.dismiss()
        }

        /**
         * on page start, check if the page is the oauth callback
         */
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            if (url != null && OAUTH_CALLBACK in url) {
                val accessTokenParamIndex = url.indexOf(ACCESS_TOKEN_PARAM) + ACCESS_TOKEN_PARAM.length
                onAuthCallback(url.substring(accessTokenParamIndex))
            }
        }
    }

}
