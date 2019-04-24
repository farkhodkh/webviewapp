package ru.carport.app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.carport.app.presenters.WebViewPresenter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var webViewPresenter: WebViewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.getComponent().injectActivity(this)

        webViewPresenter.prepareWebView(this)
    }

    fun toast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    fun toast(toastText: Int) {
        Toast.makeText(this, getString(toastText), Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (webViewPresenter.webView.canGoBack()) webViewPresenter.webView.goBack()
        else super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        webViewPresenter.onActivityResult(requestCode, resultCode, data)
    }
}
